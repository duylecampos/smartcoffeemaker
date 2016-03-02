import redis
import json
from flask import Flask, make_response

app = Flask(__name__)

@app.route("/status")
def status():
    status = {'coffeemaker': db.get('status')}
    return make_response(json.dumps(status), 200)

@app.route("/make")
def make():
    db.set('status', 'making')
    status = {'coffeemaker': db.get('status')}
    return make_response(json.dumps(status), 201)

@app.route("/stop")
def stop():
    db.set('status', 'waiting')
    status = {'coffeemaker': db.get('status')}
    return make_response(json.dumps(status), 201)

if __name__ == "__main__":
    db = redis.StrictRedis(host='localhost', port=6379, db=0)
    app.run(host='0.0.0.0', debug=True)