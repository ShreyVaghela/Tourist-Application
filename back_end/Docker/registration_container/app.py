from flask import Flask, request, jsonify
from flask_mysqldb import MySQL
from flask_cors import CORS
import json


app = Flask(__name__)
#  allow cross origin
CORS(app)

# database configuration to establish the connection to database
app.config['MYSQL_HOST'] = 'database-1.cev35euj80dg.us-east-1.rds.amazonaws.com'
app.config['MYSQL_USER'] = 'proj5409'
app.config['MYSQL_PASSWORD'] = 'proj5409'
app.config['MYSQL_DB'] = 'new_schema'

mysql = MySQL(app)

@app.route('/registration/', methods=['POST'])
def insertUserDetails():
    data = request.get_json()
    cur = mysql.connection.cursor()
    cur.execute(
        'insert into users (name, email, password, dob, sex) values (%s,%s,%s,%s,%s)',(data['name'],data['email'],data['password'],data['dob'],data['sex']))
    mysql.connection.commit()
    cur.close()
    return {'response':"Data successfully inserted in DB"}

if __name__ == '__main__':
    app.run(host='127.0.0.1',debug=True, port=5006)
