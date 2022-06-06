package justadeni.colorblock;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public final class ColorBlock extends JavaPlugin implements Listener {

    static boolean regionSupport;
    static boolean Sounds;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        if (this.getConfig().getBoolean("WorldGuardHook")) {
            try {
                final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                final RegionQuery query = container.createQuery();
                System.out.println("WorldGuard support enabled");
                regionSupport = true;
            } catch (NoClassDefFoundError e) {
                System.out.println("WorldGuard not found");
                regionSupport = false;
            }

            Sounds = getConfig().getBoolean("Sound");
        }

        getConfig();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getConfig();
        reloadConfig();
        saveConfig();
    }

    private void playNotePling(Player player){
        float leftLimit = 0.5F;
        float rightLimit = 2F;
        float generatedFloat = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, generatedFloat);
    }

    public static class WG implements Listener {
        private static boolean existWG() {
            return Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
        }

        private static boolean isInRegion(Player p) {
            if (existWG()) {
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                if (BukkitAdapter.adapt(Bukkit.getWorld(p.getWorld().getName())) != null) {
                    RegionManager region = container.get(BukkitAdapter.adapt(Bukkit.getWorld(p.getWorld().getName())));
                    boolean is = false;
                    if (region != null) {
                        for (String r : region.getRegions().keySet()) {
                            if (region.getRegion(r).contains(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ())) {
                                is = true;
                            }
                        }
                    }
                    return is;
                }
                return false;
            }
            return false;
        }

        public static String getRegion(Player p) {
            String region = null;
            if (existWG()) {
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                if (BukkitAdapter.adapt(Bukkit.getWorld(p.getWorld().getName())) != null) {
                    RegionManager reg = container.get(BukkitAdapter.adapt(Bukkit.getWorld(p.getWorld().getName())));
                    if (reg != null) {
                        for (String r : reg.getRegions().keySet()) {
                            if (reg.getRegion(r).contains(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ())) {
                                region = r;
                            }
                        }
                    }
                }
            }
            return region;
        }
    }



    public boolean callDyeBlock(String stringBlockMaterial, String stringDyeMaterial, Player player, Block clickedBlock, ItemStack item){
        if (!(player.isSneaking())) {
            String changedDyeMaterial = stringDyeMaterial.replaceFirst("_DYE", "");

            for (ColorEnum colorEnum : ColorEnum.values()) {
                String string = colorEnum.name();

                if (stringBlockMaterial.contains(string)) {

                    Material changedBlockMaterialMaterial = Material.getMaterial(stringBlockMaterial.replaceFirst(string, changedDyeMaterial));

                    if (changedBlockMaterialMaterial != null) {
                        if (!(changedBlockMaterialMaterial.toString().equals(stringBlockMaterial))) {

                            if (!stringBlockMaterial.contains("SHULKER_BOX")) {

                                clickedBlock.setType(changedBlockMaterialMaterial);

                            } else {
                                Inventory inv = ((ShulkerBox) clickedBlock.getState()).getSnapshotInventory();

                                clickedBlock.setType(changedBlockMaterialMaterial);

                                ((ShulkerBox) clickedBlock.getState()).getInventory().setContents(inv.getContents());
                                ((ShulkerBox) clickedBlock.getState()).update();
                            }

                            if (Sounds) {
                                playNotePling(player);
                            }

                            if (!(player.getGameMode().toString().equals("CREATIVE"))) {
                                int amount = item.getAmount();
                                item.setAmount((amount - 1));
                            }
                            if (clickedBlock.getBlockData().getMaterial().toString().endsWith("_BOX")) {
                                return true;
                            }
                        }
                    }
                } else if (stringBlockMaterial.equals("GLASS")) {
                    Material changedBlockMaterialMaterial = Material.getMaterial(changedDyeMaterial + "_STAINED_GLASS");
                    if (changedBlockMaterialMaterial != null) {
                        clickedBlock.setType(changedBlockMaterialMaterial);
                        if (Sounds) {
                            playNotePling(player);
                        }
                        if (!(player.getGameMode().toString().equals("CREATIVE"))) {
                            int amount = item.getAmount();
                            item.setAmount((amount - 1));
                        }
                        break;
                    }
                } else if (stringBlockMaterial.equals("GLASS_PANE")) {
                    Material changedBlockMaterialMaterial = Material.getMaterial(changedDyeMaterial + "_STAINED_GLASS_PANE");
                    if (changedBlockMaterialMaterial != null) {
                        clickedBlock.setType(changedBlockMaterialMaterial);
                        if (Sounds) {
                            playNotePling(player);
                        }
                        if (!(player.getGameMode().toString().equals("CREATIVE"))) {
                            int amount = item.getAmount();
                            item.setAmount((amount - 1));
                        }
                        break;
                    }
                } else if (stringBlockMaterial.equals("SHULKER_BOX")) {
                    Material changedBlockMaterialMaterial = Material.getMaterial(changedDyeMaterial + "_SHULKER_BOX");
                    if (changedBlockMaterialMaterial != null) {

                        //Location loc = clickedBlock.getLocation();
                        Inventory inv = ((ShulkerBox) clickedBlock.getState()).getSnapshotInventory();

                        clickedBlock.setType(changedBlockMaterialMaterial);

                        ((ShulkerBox) clickedBlock.getState()).getInventory().setContents(inv.getContents());
                        ((ShulkerBox) clickedBlock.getState()).update();

                        if (Sounds) {
                            playNotePling(player);
                        }
                        if (!(player.getGameMode().toString().equals("CREATIVE"))) {
                            int amount = item.getAmount();
                            item.setAmount((amount - 1));
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean callUnDyeBlock(String stringBlockMaterial, Player player, Block clickedBlock){

        for (ColorEnum colorEnum : ColorEnum.values()) {
            String string = colorEnum.name();

            if (stringBlockMaterial.contains(string)) {

                if (clickedBlock.getBlockData().getMaterial().toString().endsWith("_PANE")){
                    clickedBlock.setType(Material.GLASS_PANE);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.valueOf(string + "_DYE")));
                    if (Sounds){
                        playNotePling(player);
                    }
                    break;
                } else if (clickedBlock.getBlockData().getMaterial().toString().endsWith("GLASS")){
                    clickedBlock.setType(Material.GLASS);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.valueOf(string + "_DYE")));
                    if (Sounds){
                        playNotePling(player);
                    }
                    break;
                } else if (clickedBlock.getBlockData().getMaterial().toString().endsWith("_SHULKER_BOX")){

                    //Location loc = clickedBlock.getLocation();
                    Inventory inv = ((ShulkerBox) clickedBlock.getState()).getSnapshotInventory();

                    clickedBlock.setType(Material.SHULKER_BOX);

                    ((ShulkerBox) clickedBlock.getState()).getInventory().setContents(inv.getContents());
                    ((ShulkerBox) clickedBlock.getState()).update();

                    player.getInventory().setItemInMainHand(new ItemStack(Material.valueOf(string + "_DYE")));
                    if (Sounds){
                        playNotePling(player);
                    }
                    return true;
                } else {
                    if (!string.equals("LIGHT_GRAY") && !string.equals("LIGHT_BLUE")) {
                        clickedBlock.setType(Material.valueOf(stringBlockMaterial.replaceFirst(string, "WHITE")));
                        player.getInventory().setItemInMainHand(new ItemStack(Material.valueOf(string + "_DYE")));
                        if (Sounds){
                            playNotePling(player);
                        }
                        break;
                    } else if (string.startsWith("LIGHT_")){
                        clickedBlock.setType(Material.valueOf(stringBlockMaterial.replaceFirst(string, "WHITE")));
                        player.getInventory().setItemInMainHand(new ItemStack(Material.valueOf(string + "_DYE")));
                        if (Sounds){
                            playNotePling(player);
                        }
                        break;
                    }
                }

            }
        }

        return false;
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e){
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            String blockname = e.getClickedBlock().getBlockData().getMaterial().toString();
            boolean endswith = blockname.endsWith("_POWDER") ||
                    blockname.endsWith("_PANE") ||
                    blockname.endsWith("GLASS") ||
                    blockname.endsWith("_BOX") ||
                    blockname.endsWith("_CONCRETE") ||
                    blockname.endsWith("_TERRACOTTA") ||
                    blockname.endsWith("_WOOL");

            if (e.getItem() != null) {
                if (e.getItem().toString().contains("DYE")) {
                    if(e.getPlayer().hasPermission("colorblock.dye")) {

                        if (regionSupport) {
                            if (WG.isInRegion(e.getPlayer())) {
                                if (e.getPlayer().hasPermission("colorblock.worldguard")) {

                                    for (int i = 2; i >= 1; ) {
                                        i--;
                                        if (endswith) {

                                            Material BlockMaterial = e.getClickedBlock().getType();
                                            Material DyeMaterial = e.getItem().getType();

                                            String stringBlockMaterial = BlockMaterial.toString();
                                            String stringDyeMaterial = DyeMaterial.toString();

                                            if (callDyeBlock(stringBlockMaterial, stringDyeMaterial, e.getPlayer(), e.getClickedBlock(), e.getItem())) {
                                                e.setCancelled(true);
                                            }
                                        }
                                    }
                                }
                            } else if (!(WG.isInRegion(e.getPlayer()))){
                                for (int i = 2; i >= 1; ) {
                                    i--;
                                    if (endswith) {
                                        Material BlockMaterial = e.getClickedBlock().getType();
                                        Material DyeMaterial = e.getItem().getType();

                                        String stringBlockMaterial = BlockMaterial.toString();
                                        String stringDyeMaterial = DyeMaterial.toString();

                                        if (callDyeBlock(stringBlockMaterial, stringDyeMaterial, e.getPlayer(), e.getClickedBlock(), e.getItem())) {
                                            e.setCancelled(true);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (int i = 2; i >= 1; ) {
                                i--;
                                if (endswith) {
                                    Material BlockMaterial = e.getClickedBlock().getType();
                                    Material DyeMaterial = e.getItem().getType();

                                    String stringBlockMaterial = BlockMaterial.toString();
                                    String stringDyeMaterial = DyeMaterial.toString();

                                    if (callDyeBlock(stringBlockMaterial, stringDyeMaterial, e.getPlayer(), e.getClickedBlock(), e.getItem())) {
                                        e.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (e.getPlayer().isSneaking()){
                    if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
                        if (e.getPlayer().hasPermission("colorblock.undye")) {
                            if (regionSupport) {
                                if (WG.isInRegion(e.getPlayer())) {
                                    if (e.getPlayer().hasPermission("colorblock.worldguard")) {
                                        if (endswith) {
                                            Material BlockMaterial = e.getClickedBlock().getType();
                                            String stringBlockMaterial = BlockMaterial.toString();

                                            if (callUnDyeBlock(stringBlockMaterial, e.getPlayer(), e.getClickedBlock())) {
                                                e.setCancelled(true);
                                            }
                                        }
                                    }
                                } else {
                                    if (endswith) {
                                        Material BlockMaterial = e.getClickedBlock().getType();
                                        String stringBlockMaterial = BlockMaterial.toString();

                                        if (callUnDyeBlock(stringBlockMaterial, e.getPlayer(), e.getClickedBlock())) {
                                            e.setCancelled(true);
                                        }
                                    }
                                }
                            } else {
                                if (endswith) {
                                    Material BlockMaterial = e.getClickedBlock().getType();
                                    String stringBlockMaterial = BlockMaterial.toString();

                                    if (callUnDyeBlock(stringBlockMaterial, e.getPlayer(), e.getClickedBlock())) {
                                        e.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
