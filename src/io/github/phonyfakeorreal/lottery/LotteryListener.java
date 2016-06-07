package io.github.phonyfakeorreal.lottery;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class LotteryListener implements Listener {

	JavaPlugin _plugin;
	Economy _eco;
	LotteryConfiguration _config;
	  
	public LotteryListener(JavaPlugin plugin, Economy eco, LotteryConfiguration config)
	{
		_plugin = plugin;
		_eco = eco;
		_config = config;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockClick(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		 
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (e.getClickedBlock().getType() == Material.WALL_SIGN)
			{
				Block b = e.getClickedBlock();
				Sign sign = (Sign) b.getState();
				
				if (sign.getLine(0).equalsIgnoreCase("[Lottery]") && e.getPlayer().hasPermission("lottery.create"))
				{
					sign.setLine(0, "§a§l[Lottery]");
					sign.setLine(1, "§aClick to start!");
					sign.setLine(2, "");
					sign.setLine(3, "");
					sign.update();
					p.sendMessage("§a§lLottery created!");
					return;
				}
				
				if (sign.getLines()[0].contains("[Lottery]") && sign.getLines()[1].contains("Click to start!"))
				{
					BlockFace rel = getFaceFromData(sign.getData());
					Block original = b.getRelative(rel);

					
					List<Block> jukeboxes = new ArrayList<Block>();
					jukeboxes.add(original);
					
					boolean valid = true;
					if (original.getType() != Material.JUKEBOX) valid = false;
					for (BlockFace bl : getFaces(rel))
					{
						if (original.getRelative(bl).getType() != Material.JUKEBOX) valid = false;
						jukeboxes.add(original.getRelative(bl));
						break;
					}

					List<ItemFrame> frames = new ArrayList<ItemFrame>();
					
					if (valid)
					{
						for (Block juke : jukeboxes)
						{
							Entity en = juke.getWorld().spawnFallingBlock(juke.getLocation().add(0,1,0), Material.AIR, (byte) 0);
							for (Entity near : en.getNearbyEntities(.2D, 1D, .2D))
							{
								if (near.getType() == EntityType.ITEM_FRAME)
								{
									if (!frames.contains(near))
									{
										ItemFrame itemF = (ItemFrame) near;
										frames.add(itemF);
									}
								}
							}
							en.remove();
						}
						
						if (frames.size() != 3)
						{
							valid = false;
						}
						
						if (valid)
						{
							
							double bal = _eco.getBalance(e.getPlayer());
							if (bal >= 50)
							{
								ItemFrame[] array = new ItemFrame[frames.size()];
								frames.toArray(array);
								_eco.withdrawPlayer(p, _config.getCost());
								p.sendMessage("§cYou paid $" + _config.getCost() + " to enter the lottery. New balance: $" + _eco.getBalance(e.getPlayer()));
								new PlayerLottery(this._plugin, e.getPlayer(), this._config, array, this._eco, (Sign) e.getClickedBlock().getState());
							}
							else
							{
								p.sendMessage("§cYou don't have enough money to enter the lottery!");
							}
							
						}
						
					}
					
					if (!valid)
					{
						p.sendMessage("§cOops, something went wrong!");
					}
					
				}
				
				
			}
			
		}

	}
	
	@SuppressWarnings("deprecation")
	public BlockFace getFaceFromData(MaterialData d)
	{
		switch(d.getData())
		{
			case (byte)4:
				return BlockFace.EAST;
			case (byte)2:
				return BlockFace.SOUTH;
			case (byte)3:
				return BlockFace.NORTH;
			case (byte)5:
				return BlockFace.WEST;
			default:
				return null;
		}
	}
	
	public BlockFace[] getFaces(BlockFace face)
	{
		switch(face)
		{
			case NORTH:
				return new BlockFace[] {BlockFace.EAST, BlockFace.WEST};
				
			case SOUTH:
				return new BlockFace[] {BlockFace.EAST, BlockFace.WEST};
				
			case EAST:
				return new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH};
				
			case WEST:
				return new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH};
				
			default:
				return null;
		}
	}
	
}
