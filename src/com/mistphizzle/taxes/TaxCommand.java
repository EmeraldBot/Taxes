package com.mistphizzle.taxes;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class TaxCommand {

	Taxes plugin;

	public TaxCommand(Taxes instance) {
		this.plugin = instance;
		init();
	}

	private void init() {
		PluginCommand tax = plugin.getCommand("tax");
		CommandExecutor exe;

		exe = new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
				if (args.length < 1) {
					double balance = Taxes.ep.getBalance(s.getName());
					int low = plugin.getConfig().getInt("requirements.low");
					int middle = plugin.getConfig().getInt("requirements.middle");
					int high = plugin.getConfig().getInt("requirements.high");
					double lowpercent = plugin.getConfig().getDouble("taxbrackets.low") * 100;
					double middlepercent = plugin.getConfig().getDouble("taxbrackets.middle") * 100;
					double highpercent = plugin.getConfig().getDouble("taxbrackets.high") * 100;
					double lowtax = balance * plugin.getConfig().getDouble("taxbrackets.low");
					double middletax = balance * plugin.getConfig().getDouble("taxbrackets.middle");
					double hightax = balance * plugin.getConfig().getDouble("taxbrackets.high");
					double maxlowtax = plugin.getConfig().getDouble("maxtaxes.low");
					double maxmiddletax = plugin.getConfig().getDouble("maxtaxes.middle");
					double maxhightax = plugin.getConfig().getDouble("maxtaxes.high");

					if (balance >= low && balance < middle) {
						String rounded = Taxes.ep.format(lowtax);
						s.sendMessage("§3Tax Bracket: §aLow");
						s.sendMessage("§3Tax Percent: §a" + lowpercent + "%");
						if (lowtax > maxlowtax) {
							s.sendMessage("§3Taxes Due: §a" + maxlowtax);
						} else {
							s.sendMessage("§3Taxes Due: §a" + rounded);
						}
					}
					if (balance >= middle && balance < high) {
						String rounded = Taxes.ep.format(middletax);
						s.sendMessage("§3Tax Bracket: §aMiddle");
						s.sendMessage("§3Tax Percent: §a" + middlepercent + "%");
						if (middletax > maxmiddletax) {
							s.sendMessage("§3Taxes Due: §a" + maxmiddletax);
						} else {
							s.sendMessage("§3Taxes Due: §a" + rounded);
						}
					}

					if (balance >= high) {
						s.sendMessage("§3Tax Bracket: §aHigh");
						String rounded = Taxes.ep.format(hightax);
						s.sendMessage("§3Tax Percent: §a" + highpercent + "%");
						if (hightax > maxhightax) {
							s.sendMessage("§3Taxes Due: §a" + maxhightax);
						} else {
							s.sendMessage("§3Taxes Due: §a" + rounded);
						}
					}
					s.sendMessage("§3Last Paid Taxes: §a" + plugin.getConfig().getString("players." + s.getName()));
					s.sendMessage("§3Balance: §a" + balance);
					if (s.hasPermission("taxes.exempt")) {
						s.sendMessage("§3Status: §cExempt");
					} else if (!s.hasPermission("taxes.exempt")) {
						s.sendMessage("§3Status: §aApplicable");
					}
					return true;
				} if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						if (!s.hasPermission("taxes.reload")) {
							s.sendMessage("§cYou don't have permission to do that!");
							return true;
						} else {
							plugin.reloadConfig();
							s.sendMessage("§aTaxes Configuration Reloaded.");
						}
					}
					if (args[0].equalsIgnoreCase("collect")) {
						if (!s.hasPermission("taxes.collect")) {
							s.sendMessage("§cYou don't have permission to do that!");
							return true;
						} else {
							for(Player player: Bukkit.getOnlinePlayers()) {
								plugin.CollectTaxes(player);
							}
							return true;
						}
					}
					return true;
				}
				return true;
			}
		}; tax.setExecutor(exe);


	}
}
