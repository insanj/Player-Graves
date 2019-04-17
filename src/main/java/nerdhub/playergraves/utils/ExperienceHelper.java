package nerdhub.playergraves.utils;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExperienceHelper {

    public static void deserializeExp(PlayerEntity player, ListTag expTag) {
      CompoundTag metadata = expTag.getCompoundTag(0);
      int experience = metadata.getInt("experience");

    /*  if (experience > 7) {
        experience -= 7;
      } else { //} if (expLevel <= 7) {
        player.addChatMessage(new TranslatableTextComponent(String.format("No experience has been collected because the gravestone will only save your levels above 7.")).setStyle(new Style().setColor(TextFormat.GOLD)), false);
        return;
      }*/

      if (!player.world.isClient && player instanceof ServerPlayerEntity) {
        ServerPlayerEntity splayer = (ServerPlayerEntity)player;
        int currentExp = splayer.experienceLevel;
        int combinedExp = currentExp + experience;
        splayer.setExperienceLevel(combinedExp);

        System.out.println(String.format("[Player-Graves] Deserialized experience for player %s: %d (current) + %d (saved) = %d", player.toString(), currentExp, experience, combinedExp));

       // player.world.spawnEntity(new ExperienceOrbEntity(player.world, player.x, player.y, player.z, experience));
      }

      // player.addChatMessage(new TranslatableTextComponent(String.format("Level up! You have regained %d levels.", expLevel)).setStyle(new Style().setColor(TextFormat.GOLD)), false);
    }

    public static ListTag serialize(PlayerEntity player, ListTag listTag) {
      int experience = player.experienceLevel;

      System.out.println(String.format("[Player-Graves] Serializing experience from player %s: %d (this happens twice; once for the Gravestone which immediately spawns, once for the save file in case the user wishes to use the recover command)", player.toString(), experience));

      CompoundTag metadata = new CompoundTag();
      metadata.putInt("experience", experience);

      listTag.add(metadata);
      return listTag;
    }
}
