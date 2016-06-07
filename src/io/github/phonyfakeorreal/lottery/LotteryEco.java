package io.github.phonyfakeorreal.lottery;

import net.milkbowl.vault.economy.Economy;

public class LotteryEco {

	private Economy _econ;
	
	public LotteryEco(Economy eco)
	{
		_econ = eco;
	}
	
	public Economy getEco()
	{
		return this._econ;
	}
	
}
