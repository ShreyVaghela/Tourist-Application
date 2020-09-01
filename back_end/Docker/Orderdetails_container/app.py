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


# fetch order history of a particular use
@app.route('/<id>')
def getOrderDetails(id):
    cur = mysql.connection.cursor()
    cur.execute('SELECT t.id as id, a1.name as source_id, a2.name as dest_id , t.date, t.num_passengers FROM trips t INNER JOIN address a1 ON a1.id=t.source_id INNER JOIN address a2 ON a2.id=t.dest_id where t.user_id="%s"'%(str(id)))
    mysql.connection.commit()
    rows = cur.fetchall()
    result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
    cur.close()
    return {'items':result}

if __name__ == '__main__':
    app.run(host='127.0.0.1',debug=True, port=5002)