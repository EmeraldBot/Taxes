package com.mistphizzle.taxes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TaxListener implements Listener {

	public static Taxes plugin;

	public TaxListener(Taxes instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent e) {
		Player player = e.getPlayer();
		plugin.CollectTaxes(player);
	}
}
