package nerdhub.playergraves.data;

import com.google.common.collect.Maps;
import nerdhub.playergraves.utils.InventoryHelper;
import nerdhub.playergraves.utils.ExperienceHelper;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TagHelper;
import net.minecraft.world.PersistentState;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;

import java.util.*;

public class PlayerInventoryPersistentState extends PersistentState {
    static final String PERSISTENT_STATE_ID = "PlayerGraves110InventoryPersistentState";

    private Map<UUID, ListTag> inventories = Maps.newHashMap();
    private Map<UUID, ListTag> experiences = Maps.newHashMap();

    public PlayerInventoryPersistentState() {
        super(PlayerInventoryPersistentState.PERSISTENT_STATE_ID);
    }

    public void recoverPlayerInventory(ServerPlayerEntity playerEntity) {
        InventoryHelper.deserializeInv(playerEntity, getPlayerInventory(playerEntity));
        ExperienceHelper.deserializeExp(playerEntity, getPlayerExperience(playerEntity));
    }

    public ListTag getPlayerInventory(ServerPlayerEntity playerEntity) {
        return this.inventories.get(playerEntity.getUuid());
    }

    public ListTag getPlayerExperience(ServerPlayerEntity playerEntity) {
      return this.experiences.get(playerEntity.getUuid());
    }

    public boolean isPlayerInventorySaved(ServerPlayerEntity playerEntity) {
        return this.inventories.containsKey(playerEntity.getUuid());
    }

    public void savePlayerInventory(ServerPlayerEntity playerEntity) {
        this.inventories.put(playerEntity.getUuid(), playerEntity.inventory.serialize(new ListTag()));
        this.experiences.put(playerEntity.getUuid(), ExperienceHelper.serialize(playerEntity));
        
        this.markDirty();
    }

    public static PlayerInventoryPersistentState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(PlayerInventoryPersistentState::new, PlayerInventoryPersistentState.PERSISTENT_STATE_ID);
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        ListTag listTag = compoundTag.getList("inventories", NbtType.COMPOUND);

        for (Iterator<Tag> it = listTag.iterator(); it.hasNext();) {
            CompoundTag tag = (CompoundTag) it.next();
            UUID uuid = TagHelper.deserializeUuid(tag.getCompound("uuid"));
            ListTag inventoryTag = tag.getList("inventory", NbtType.COMPOUND);
            inventories.put(uuid, inventoryTag);
        }

        listTag = compoundTag.getList("experiences", NbtType.COMPOUND);

        for (Iterator<Tag> it = listTag.iterator(); it.hasNext();) {
            CompoundTag tag = (CompoundTag) it.next();
            UUID uuid = TagHelper.deserializeUuid(tag.getCompound("uuid"));
            ListTag expTag = tag.getList("experience", NbtType.COMPOUND);
            experiences.put(uuid, expTag);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();

        for (UUID uuid : inventories.keySet()) {
            CompoundTag tag = new CompoundTag();
            tag.put("uuid", TagHelper.serializeUuid(uuid));
            tag.put("inventory", inventories.get(uuid));
            listTag.add(tag);
        }

        compoundTag.put("inventories", listTag);

        ListTag listExpTag = new ListTag();

        for (UUID uuid : experiences.keySet()) {
            CompoundTag tag = new CompoundTag();
            tag.put("uuid", TagHelper.serializeUuid(uuid));
            tag.put("experience", experiences.get(uuid));
            listExpTag.add(tag);
        }

        compoundTag.put("experiences", listExpTag);

        return compoundTag;
    }
}
