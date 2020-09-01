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


# add invoice on successul booking 
def createInvoice(id, total):
    cur=mysql.connection.cursor()
    date=pd.to_datetime('today')#.strftime('%Y-%m-%d')
    time=pd.Timestamp('today')
    cur.execute("INSERT INTO invoice (`trip_id`, `date`, `time`, `amount`) VALUES (%s, %s, %s, %s)",(id,date,time,total))    
    invoice_id = cur.lastrowid
    mysql.connection.commit()
    cur.close()
    return invoice_id



# payment gateway to verify card details
@app.route('/makePayment',methods=['POST'])
def validate_card():
    user_id=(request.form['userId'])
    source_id=(request.form['source_id'])
    dest_id=(request.form['dest_id'])
    bus_id=request.form['bus_id']
    price=float(request.form["price"])
    date=request.form['date']
    num_passengers=(request.form['numPass'])
    
    cardNumber=request.form["cardNumber"]
    cardName=request.form["cardName"]
    expiryDate=request.form["expiryDate"]
    cardCVV=request.form["cvCode"]
    
    if(validateCard(cardNumber,expiryDate,cardCVV)):
        # add trip to database on successful payment
        cur=mysql.connection.cursor()
        cur.execute('''INSERT INTO `trips` ( `user_id`, `source_id`, `dest_id`, `date`,  `num_passengers`, `bus_id`) VALUES (%s, %s, %s, %s,  %s, %s)''',(user_id,source_id,dest_id,date,num_passengers,bus_id))
        trip_id = cur.lastrowid
        # update number of seats available in bus table
        cur.execute(''' UPDATE bus SET num_bookings = num_bookings+%s WHERE id = %s''',(num_passengers,bus_id))
        mysql.connection.commit()
        #  create invoice for the trip
        invoice_id=createInvoice(trip_id, (float(num_passengers)*price))
        cur.execute(""" select t.date as travel_date,u.name as user,i.date as booking_date ,a1.name  as source, a2.name as destination , t.num_passengers, b.bus_no, b.arr_time, b.dep_time, b.price as unit_price, i.amount as total from  invoice i 
        inner join trips t on t.id=i.trip_id 
        inner join bus b on b.id=t.bus_id 
        inner join address a1 on a1.id=t.source_id 
        inner join address a2 on a2.id=t.dest_id
        inner join users u on t.user_id=u.email 
        where i.invoice_no="""+str(invoice_id))
        
        rows=cur.fetchall()
        result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
        return render_template("invoice.html", invoice_id=invoice_id, result=result[0])
    else:
        return 'Payment failed. Please check your card details.'


@app.route('/mobileMakePayment',methods=['POST'])
def mobile_validate_card():
	user_id=(request.form['userId'])
	source_id=(request.form['source_id'])
	dest_id=(request.form['dest_id'])
	bus_id=request.form['bus_id']
	price=float(request.form["price"])
	date=request.form['date']
	num_passengers=(request.form['numPass'])

	cardNumber=request.form["cardNumber"]
	cardName=request.form["cardName"]
	expiryDate=request.form["expiryDate"]
	cardCVV=request.form["cvCode"]

	if(validateCard(cardNumber,expiryDate,cardCVV)):
		# add trip to database on successful payment
		cur=mysql.connection.cursor()
		cur.execute('''INSERT INTO `trips` ( `user_id`, `source_id`, `dest_id`, `date`,  `num_passengers`, `bus_id`) VALUES (%s, %s, %s, %s,  %s, %s)''',(user_id,source_id,dest_id,date,num_passengers,bus_id))
		trip_id = cur.lastrowid
		# update number of seats available in bus table
		cur.execute(''' UPDATE bus SET num_bookings = num_bookings+%s WHERE id = %s''',(num_passengers,bus_id))
		mysql.connection.commit()
		#  create invoice for the trip
		invoice_id=createInvoice(trip_id, (float(num_passengers)*price))
		cur.execute(""" select t.date as travel_date,u.name as user,i.date as booking_date ,a1.name  as source, a2.name as destination , t.num_passengers, b.bus_no, b.arr_time, b.dep_time, b.price as unit_price, i.amount as total from  invoice i 
		inner join trips t on t.id=i.trip_id 
		inner join bus b on b.id=t.bus_id 
		inner join address a1 on a1.id=t.source_id 
		inner join address a2 on a2.id=t.dest_id
		inner join users u on t.user_id=u.email 
		where i.invoice_no="""+str(invoice_id))
		
		rows=cur.fetchall()
		result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
		return jsonify(rows)
	else:
		return jsonify(0)
		
# card details validation
def validateCard(cardNumber,cardDate,cardCVV):
    return (cardNumber=="1111111111111111" and cardDate=="00/00" and cardCVV=="999")
         

if __name__ == '__main__':
    app.run(host='0.0.0.0',debug=True, port=5005)
