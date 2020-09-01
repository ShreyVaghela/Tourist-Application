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

# fetch list of buses from selected source to destination
@app.route('/getBuses/<sourceId>/<destId>',methods=['GET'])
def get_invoice(sourceId,destId):
    cur = mysql.connection.cursor()
    # cur.execute('select id,bus_no, arr_time,dep_time,capacity - num_bookings as seats from bus where source_id=%s and dest_id=%s and capacity <> num_bookings' %(sourceId,destId))
    cur.execute('''select b.id, a1.id as src_id, a2.id as dest_id ,a1.name as src,a2.name as dest,bus_no, arr_time,dep_time,(capacity - num_bookings) 
    as seats, b.price from bus b INNER JOIN address a1 ON a1.id=b.source_id INNER JOIN address a2 ON a2.id=b.dest_id 
    where source_id=%s and dest_id=%s and capacity <> num_bookings''' %(sourceId,destId))
    mysql.connection.commit()
    rows=cur.fetchall()
    result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
    cur.close()
    return jsonify({'result': result})


# get locations as source
@app.route('/getSources/<destId>')
def getSources(destId):
    cur = mysql.connection.cursor()
    cur.execute('select id as sourceId, name from address where id <>'+destId)
    mysql.connection.commit()
    rows=cur.fetchall()
    result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
    cur.close()
    return jsonify({'result': result})


if __name__ == '__main__':
    app.run(host='127.0.0.1',debug=True, port=5004)