package nerdhub.playergraves.utils;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class ExperienceHelper {

    public static void deserializeExp(PlayerEntity player, CompoundTag expTag) {
      int exp = expTag.getInt("experience"); //i
      int expLevel = expTag.getInt("experienceLevel"); //i
      float expBar = expTag.getFloat("experienceBarProgress"); //f

      if (!player.world.isClient) {
        player.world.spawnEntity(new ExperienceOrbEntity(player.world, player.x, player.y, player.z, exp));
      } else {
        player.addExperience(exp);
      }
    }
}
