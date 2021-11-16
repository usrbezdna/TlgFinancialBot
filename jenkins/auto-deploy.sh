building_dir="/var/lib/jenkins/workspace/TelegramBot/target/"
jar_name=$(ls $building_dir | grep jar-with-dependencies.jar)


if [ -z $(ls /etc/systemd/system | grep bot-daemon.service) ] 
then 
	echo "[Unit]
	Description=Telegram Bot
    Documentation=https://github.com/tooBusyNow/tlgFinancialBot
	After=network.target
    
    [Service]
    User=root
    Type=simple
    Restart=on-failure
    RestartSec=30
    WorkingDirectory=$building_dir
    EnvironmentFile=/etc/environment.d/bot-d.conf
    ExecStartPre=/bin/bash -c "echo Setting up a BotService... "
    ExecStart=java -jar $jar_name
    ExecStartPost=/bin/bash -c "echo BotService was set "
    
    
    [Install]
	WantedBy=multi-user.target" > $HOME/bot-daemon.service
  
    echo bezdna | sudo -S mv $HOME/bot-daemon.service /etc/systemd/system
fi

status=$(/bin/bash -c "service bot-daemon status 2>/dev/null | grep active")

if [ -z "$status" ]
then
	echo -e "\033[1;45m BotService is Starting... \033[0m"
    echo bezdna | sudo -S systemctl start bot-daemon
else	
	echo -e "\033[1;45m BotService is Restarting... \033[0m"   
    echo bezdna | sudo -S systemctl restart bot-daemon
fi

echo -e "\033[1;45m Deployment was finished \033[0m"
