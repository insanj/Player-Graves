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
      int exp = expTag.getInt(0);
      int expLevel = expTag.getInt(1);
      float expBar = expTag.getFloat(2);

      player.addChatMessage(new TranslatableTextComponent(String.format("%03d %03d %f", exp, expLevel, expBar)).setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), false);

      if (!player.world.isClient) {
        player.world.spawnEntity(new ExperienceOrbEntity(player.world, player.x, player.y, player.z, exp));
      } else {
        player.addExperience(exp);
      }
    }

    public static ListTag serialize(PlayerEntity player) {
      player.addChatMessage(new TranslatableTextComponent(String.format("serializing experience: %03d %03d %f", player.experience, player.experienceLevel, player.experienceBarProgress)).setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), false);

      ListTag exp = new ListTag();

      IntTag experienceTag = new IntTag(player.experience);
      IntTag experienceLevelTag = new IntTag(player.experienceLevel);
      FloatTag experienceBarProgressTag = new FloatTag(player.experienceBarProgress);

      exp.add(experienceTag);
      exp.add(experienceLevelTag);
      exp.add(experienceBarProgressTag);

      return exp;
    }
}
