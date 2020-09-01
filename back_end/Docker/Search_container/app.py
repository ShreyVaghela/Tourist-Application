from flask import Flask, render_template, request,render_template, jsonify,render_template_string
from flask_mysqldb import MySQL
from flask_cors import CORS
import plotly
import plotly.graph_objs as go

import pandas as pd
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


# fetch locations from database
@app.route('/search/<loc>')
def index(loc):
    cur = mysql.connection.cursor()
    cur.execute('SELECT id, name, price, description, image, address, highlights, address_id from location WHERE name LIKE %s OR address LIKE %s OR highlights LIKE %s', ("%"+loc+"%", "%"+loc+"%", "%"+loc+"%"))
    mysql.connection.commit()
    rows = cur.fetchall()
    # convert query result to json
    items = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
    cur.close()
    return {'items':items}


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True,port=5001)