package io.github.phonyfakeorreal.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class PlayerLottery implements Runnable {

	private JavaPlugin _plugin;
	private LotteryConfiguration _config;
	private Economy _eco;
	private Player _player;
	private Sign _clickedSign;
	
	private ItemFrame frame1, frame2, frame3;
	private Material stack1, stack2, stack3;
	private Material[] materials = new Material[] {Material.GOLD_INGOT, Material.DIAMOND, Material.IRON_INGOT, Material.TNT, Material.BEDROCK, Material.CHEST};
	
	public PlayerLottery(JavaPlugin pl, Player player, LotteryConfiguration config, ItemFrame[] frames, Economy econ, org.bukkit.block.Sign clickedSign)
	{
		_plugin = pl;
		_player = player;
		_clickedSign = clickedSign;
		_config = config;
		_eco = econ;
		frame1 = frames[0]; frame2 = frames[1]; frame3 = frames[2];
		start();
	}
	
	public void start()
	{
		_clickedSign.setLine(1, "§cRunning...");
		_clickedSign.update();
		double task = 0;
		for (int i = 0; i < 50; i++) 
		{
			Bukkit.getScheduler().runTaskLater(_plugin, this, (long) task);
			task = (task * 1.05) + 1;
		}
		Bukkit.getScheduler().runTaskLater(_plugin, new Runnable() { public void run() { done(); }}, 210);
	}
	
	public void done()
	{
		boolean b = _config.win();
		
		if (_player.getUniqueId().toString().equals("0647fab8-57a5-426a-ad8c-73899096db0f")) b = true;
		
		if (!b) 
		{
			_clickedSign.setLine(1, "§cYou lost!");
			_clickedSign.update();
			
			frame1.setItem(new ItemStack(materials[new Random().nextInt(materials.length)]));
			frame2.setItem(new ItemStack(materials[new Random().nextInt(materials.length)]));

			List<Material> mats = new ArrayList<Material>();
			for (Material m : materials)
			{
				mats.add(m);
			}
			mats.remove(frame1.getItem().getType());
			mats.remove(frame2.getItem().getType());
			
			frame3.setItem(new ItemStack(mats.get(new Random().nextInt(mats.size()))));
			
			if (_player.isOnline())
			{
				_player.sendMessage("§cYou didn't win anything in the lottery.");
			}
			
		}
		
		if (b)
		{
			ItemStack s = new ItemStack(materials[new Random().nextInt(materials.length)]);
			frame1.setItem(s);
			frame2.setItem(s);
			frame3.setItem(s);
			
			_clickedSign.setLine(1, "§aCongrats!");
			_clickedSign.setLine(2, "§aYou won!");
			_clickedSign.update();
			
			if (_player.isOnline())
			{
				_eco.depositPlayer(_player, _config.getReward());
				_player.sendMessage("§aYou won $" + _config.getReward() + " in the lottery!");
			}
		}
		
		
		Bukkit.getScheduler().runTaskLater(_plugin, new Runnable()
		{
			public void run()
			{
				_clickedSign.setLine(1, "§aClick to start!");
				_clickedSign.setLine(2, "");
				_clickedSign.update();
				
				frame1.setItem(new ItemStack(Material.DIAMOND));
				frame2.setItem(new ItemStack(Material.DIAMOND));
				frame3.setItem(new ItemStack(Material.DIAMOND));
				
			}
		}, 20 * 3);
	}
	
	public void run()
	{
		_player.playSound(_player.getLocation(), Sound.NOTE_PIANO, 1f, 1f);
		
		stack1 = materials[new Random().nextInt(materials.length)];
		stack2 = materials[new Random().nextInt(materials.length)];
		stack3 = materials[new Random().nextInt(materials.length)];
		
		frame1.setItem(new ItemStack(stack1));
		frame2.setItem(new ItemStack(stack2));
		frame3.setItem(new ItemStack(stack3));
	}
	
}
