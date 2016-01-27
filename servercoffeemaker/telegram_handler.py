import telebot
import random
import requests

bot = telebot.TeleBot("150255215:AAE-nWXLaP0yArclvR-kjMmIkxUZyCVHE-I")

@bot.message_handler(commands=['querocafe'])
def send_welcome(message):
	if random.randint(0, 1) == 0:
		foo = ['Nope, nope, nope.', 'Talvez depois.', 'Not enough mana.', 'Voce nem bebe cafe.', 'E a impressora?']
		bot.reply_to(message, random.choice(foo))
	else:
		foo = ['Ok, logo estara pronto.', 'Te aviso quando terminar.', 'Right away, sir.']
		bot.send_message(-3935030, random.choice(foo))
		# SERVER: set 1 on global variable #

# Coffee is ready, tell everyone this
def coffee_is_ready():
	foo = ['Coffee is ready.']
	bot.send_message(-3935030, random.choice(foo))

@bot.message_handler(commands=['11h30'])
def send_welcome(message):
	foo = ['Miyoshi.', 'Pizza Hut.', '(isRoverThere ? otherPlace : Tradicao)', 'delicious.', 'Shopping.', '.. nao almoce, tome cafe.', 'Five.']
	bot.reply_to(message, 'Almoce no ' + random.choice(foo))
	print(random.choice(foo))

bot.polling()