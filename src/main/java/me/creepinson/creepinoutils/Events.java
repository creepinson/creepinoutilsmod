package me.creepinson.creepinoutils;

import com.mrcrayfish.vehicle.entity.EntityTrailer;
import com.mrcrayfish.vehicle.item.ItemVehicleTool;
import me.creepinson.creepinoutils.api.util.compat.CompatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Made By Creepinson
 */
@Mod.EventBusSubscriber(modid = CreepinoUtilsMod.MOD_ID)
public class Events {
    private static HashMap<EntityPlayer, EntityTrailer> trailers = new HashMap<>();

    /**
     * Hook for mr crayfish vehicles to allow seperate
     */
    @Optional.Method(modid = CompatUtils.CRAYFISH_VEHICLES)
    @SubscribeEvent
    public static void useItem(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getTarget();
        if (event.getItemStack().getItem() instanceof ItemVehicleTool) {
            EntityPlayer player = event.getEntityPlayer();
            if (player.isSneaking()) {
                if (entity instanceof EntityTrailer && !trailers.containsKey(player)) {
                    EntityTrailer trailer = (EntityTrailer) entity;
                    trailers.put(player, trailer);
                }
            } else {
                if (trailers.containsKey(player)) {
                    EntityTrailer trailer = trailers.get(player);
                    try {
                        if (trailer.getClass().getSuperclass() == EntityTrailer.class) {
                            Field f = trailer.getClass().getSuperclass().getDeclaredField("pullingEntity");
                            f.setAccessible(true);
                            f.set(trailer, entity);
                            trailer.getDataManager().set(EntityTrailer.PULLING_ENTITY, entity.getEntityId());
                            trailers.remove(player);
                            CreepinoUtilsMod.debug("Attached entity to trailer: " + entity.getClass().getSimpleName());
                        }
                    } catch (Exception e) {
                        CreepinoUtilsMod.debug("Could not set pulling entity of trailer.");
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
