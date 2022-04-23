package blu3.ruhamaplus.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityListUtils implements Util{

    public static List<Entity> getEntitiesOfType(Class entityBruh){
        List<Entity> entities = new ArrayList<>();
        for (Entity e : mc.world.loadedEntityList){
            if (entityBruh.isInstance(e)){
                entities.add(e);
            }
        }
        return entities;
    }

    public static List<Class> allEntitiesBut(Class... entities){
        List<Class> yoo = new ArrayList<>();
        for (Class byhufasp : someEntities){
            for (Class bruh : entities){
                if (!bruh.isInstance(byhufasp)) yoo.add(byhufasp);
            }
        }
        return yoo;
    }


    public static final List<Class> someEntities = Arrays.asList(
            EntityPlayer.class,
            EntityItem.class,
            EntityEnderCrystal.class,
            EntityCreature.class,
            EntityExpBottle.class,
            EntityEnderEye.class,
            EntityEnderPearl.class,
            EntityEnderman.class,
            EntityEndermite.class,
            EntityPigZombie.class,
            EntityZombie.class
    );

}
