package io.github.phonyfakeorreal.lottery;

import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;

public class LotteryConfiguration {

	private int reward = 0;
	private int cost = 0;
	private int max = 0;
	private int chance = 0;
	
	public LotteryConfiguration(JavaPlugin pl)
	{
		reward = pl.getConfig().getInt("reward");
		cost = pl.getConfig().getInt("cost");
		max = Integer.valueOf(pl.getConfig().getString("winchance").split("/")[1]) - 1;
		chance = Integer.valueOf(pl.getConfig().getString("winchance").split("/")[0]) - 1;
	}
	
	public int getCost()
	{
		return this.cost;
	}
	
	public int getReward()
	{
		return this.reward;
	}
	
	public boolean win()
	{
		int number = new Random().nextInt(max);
		if (number <= chance) return true;
		return false;
	}
	
}
