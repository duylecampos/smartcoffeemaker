import telebot
import random
import requests

bot = telebot.TeleBot("150255215:AAE-nWXLaP0yArclvR-kjMmIkxUZyCVHE-I")

@bot.message_handler(commands=['querocafe'])
def send_welcome(message):
	foo = ['Nope, nope, nope', 'Talvez depois', 'Ok, estara pronto assim que voce me programar', 'Wow, much coffee, very ligado', 'Not enough mana', 'Voce nem bebe cafe.', 'Under construction', 'E a impressora?', 'Tente pelo meu twitter @cafeteira_nkey']
	bot.reply_to(message, random.choice(foo))
	print(random.choice(foo))
	r = requests.get('https://api.github.com/events')
	print(r.text)


@bot.message_handler(commands=['11h30'])
def send_welcome(message):
	foo = ['Miyoshit', 'Pizza hut', 'isRoverThere ? otherPlace : Tradicao', 'Delicious of the field', 'Shopping', '.. nao almoce, tome cafe']
	bot.reply_to(message, 'Almoce no ' + random.choice(foo))
	print(random.choice(foo))

bot.polling()