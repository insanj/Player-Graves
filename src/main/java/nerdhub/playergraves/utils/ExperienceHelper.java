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

public class ExperienceHelper {

    public static void deserializeExp(PlayerEntity player, ListTag expTag) {
      CompoundTag metadata = expTag.getCompoundTag(0);
      int expLevel = metadata.getInt("experienceLevel");

      // player.addChatMessage(new TranslatableTextComponent(String.format("deserializing experience: %03d", expLevel)).setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), false);

      if (!player.world.isClient) {
        player.world.spawnEntity(new ExperienceOrbEntity(player.world, player.x, player.y, player.z, expLevel));
      } else {
        player.addExperience(expLevel);
      }
    }

    public static ListTag serialize(PlayerEntity player) {
      // player.addChatMessage(new TranslatableTextComponent(String.format("serializing experience: %03d %03d %f", player.experience, player.experienceLevel, player.experienceBarProgress)).setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), false);

      CompoundTag metadata = new CompoundTag();
      metadata.putInt("experienceLevel", player.experienceLevel);

      ListTag listTag = new ListTag();
      listTag.add(metadata);
      return listTag;
    }
}
