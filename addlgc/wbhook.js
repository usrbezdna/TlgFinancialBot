const ID = '910239243064995950'
const DISCORD_TOKEN = '0THh_yzhDw1MBxBAtnMAoQzKH58ITIovY8UP8IwaofLn8flHF1ckucYo-kuywzWbpTKZ'

const hookcord = require('hookcord');
const Hook = new hookcord.Hook()
Hook.login(ID, DISCORD_TOKEN)

const message = {

	"content" : "Deployed new version of bot",
	"color" : 1681177,
  "username": "JenkinsNotifierBot",
  "avatar_url": "https://www.pngitem.com/pimgs/m/437-4379798_jenkins-continuous-integration-hd-png-download.png",
      
}

Hook.fire()
  .then(response_object => {
  })
  .catch(error => {
    console.log(error);
  })

console.log("Sended")