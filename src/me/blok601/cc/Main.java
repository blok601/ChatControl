package me.blok601.cc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	FileConfiguration config = getConfig();

	public void onEnable() {
		getLogger().info("ChatControl by blok601 has been enabled!");
		config.options().copyDefaults(true);
		saveConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {
		getLogger().info("ChatControl by blok601 has been disabled!");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String args[]) {
		if (cmd.getName().equalsIgnoreCase("lockchat")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.hasPermission("chatcontrol.lock")) {
					if (config.getBoolean("locked")) {
						config.set("locked", false);
						saveConfig();
						p.sendMessage(ChatColor.GOLD + "[ChatControl] "
								+ ChatColor.AQUA + "The chat has been "
								+ ChatColor.RED + "unlocked!");
						Bukkit.broadcastMessage(ChatColor.GOLD
								+ "[ChatControl] " + ChatColor.AQUA
								+ "The chat has been " + ChatColor.RED
								+ "unlocked by " + p.getName() + "!");
						return true;
					} else {
						config.set("locked", true);
						saveConfig();
						p.sendMessage(ChatColor.GOLD + "[ChatControl] "
								+ ChatColor.AQUA + "The chat has been "
								+ ChatColor.GREEN + "locked!");
						Bukkit.broadcastMessage(ChatColor.GOLD
								+ "[ChatControl] " + ChatColor.AQUA
								+ "The chat has been " + ChatColor.GREEN
								+ "locked by " + p.getName() + "!");
						return true;
					}
				} else {
					p.sendMessage(ChatColor.RED
							+ "You don't have permission to lock the chat!");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Only players can do this!");
				return false;
			}
		} else if (cmd.getName().equalsIgnoreCase("clearchat")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.hasPermission("chatcontrol.clear")) {
					clearChat();
					Bukkit.broadcastMessage(ChatColor.GOLD +"[ChatControl] " + ChatColor.AQUA + "The chat has been cleared by " + p.getName());
				} else {
					p.sendMessage(ChatColor.RED
							+ "You don't have permission to clear the chat!");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Only players can do this!");
				return false;
			}
		}
		return false;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player pl = e.getPlayer();
		if (config.getBoolean("locked")) {
			if (!pl.hasPermission("chatcontrol.lock.bypass")) {
				e.setCancelled(true);
				pl.sendMessage(ChatColor.GOLD + "[ChatControl] "
						+ ChatColor.RED
						+ "You may not speak! The chat is locked!");
				return;
			}
		} else {
			return;
		}
	}

	private void clearChat() {
		for (Player pls : Bukkit.getOnlinePlayers()) {
			if (!pls.hasPermission("chatcontrol.clear.bypass")) {
				sendClear(pls);
			}
		}
	}

	private void sendClear(Player p) {
		for (int i = 0; i < 100; i++) {
			p.sendMessage(" ");
		}
	}
}
