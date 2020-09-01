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



#  fetch locations from database
@app.route('/search/<loc>', methods=['GET'])
def index(loc):
    cur = mysql.connection.cursor()
    cur.execute('SELECT id, name, price, description, image, address, highlights, address_id from location WHERE name LIKE %s OR address LIKE %s OR highlights LIKE %s', ("%"+loc+"%", "%"+loc+"%", "%"+loc+"%"))
    mysql.connection.commit()
    rows = cur.fetchall()
    # convert query result to json
    items = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
    cur.close()
    return jsonify({'items':items})


# fetch order history of a particular use
@app.route('/orderDetails/<id>', methods=['GET'])
def getOrderDetails(id):
    cur = mysql.connection.cursor()
    cur.execute('SELECT t.id as id, a1.name as source_id, a2.name as dest_id , t.date, t.num_passengers FROM trips t INNER JOIN address a1 ON a1.id=t.source_id INNER JOIN address a2 ON a2.id=t.dest_id where t.user_id="%s"'%(str(id)))
    mysql.connection.commit()
    rows = cur.fetchall()
    result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
    cur.close()
    return {'items':result}

# api to analyse the trends in data and create visulizations using plotly library
@app.route('/analytics', methods=['GET'])
def getAnalytics():
    cur = mysql.connection.cursor()
    cur.execute('select a1.name as city, t.date,count(*) as num_trips from trips t INNER JOIN address a1 ON a1.id=t.dest_id where t.dest_id  group by t.date,t.dest_id ORDER BY DATE(t.date) DESC')
    mysql.connection.commit()
    rows = cur.fetchall()
    result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
    if not result:
        return "No data to show"
    cur.close()
    line = create_plot(result)
    # create graph
    res=render_template('analytics.html', plot=line[0],ids=line[1])
    return render_template_string(res)

# fetch list of buses from selected source to destination
@app.route('/getBuses/<sourceId>/<destId>',methods=['GET'])
def get_invoice(sourceId,destId):
    cur = mysql.connection.cursor()
    # cur.execute('select id,bus_no, arr_time,dep_time,capacity - num_bookings as seats from bus where source_id=%s and dest_id=%s and capacity <> num_bookings' %(sourceId,destId))
    cur.execute('''select b.id, a1.id as src_id, a2.id as dest_id ,a1.name as src,a2.name as dest,bus_no, arr_time,dep_time,(capacity - num_bookings) 
    as seats, b.price from bus b INNER JOIN address a1 ON a1.id=b.source_id INNER JOIN address a2 ON a2.id=b.dest_id 
    where source_id=%s and dest_id=%s and capacity > num_bookings''' %(sourceId,destId))
    mysql.connection.commit()
    rows=cur.fetchall()
    result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
    cur.close()
    return jsonify({'result': result})

# get locations as source
@app.route('/getSources/<destId>', methods=['GET'])
def getSources(destId):
    cur = mysql.connection.cursor()
    cur.execute('select id as sourceId, name from address where id <>'+destId)
    mysql.connection.commit()
    rows=cur.fetchall()
    result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
    cur.close()
    return jsonify({'result': result})

# add user to the database on signup
@app.route('/registration/', methods=['POST'])
def insertUserDetails():
    data = request.get_json()
    cur = mysql.connection.cursor()
    cur.execute(
        'insert into users (name, email, password, dob, sex) values (%s,%s,%s,%s,%s)',(data['name'],data['email'],data['password'],data['dob'],data['sex']))
    mysql.connection.commit()
    cur.close()
    return {'response':"Data successfully inserted in DB"}

#  prepare data to analyse bookings for each cities based on timeline
def create_plot(data):
    df = pd.DataFrame(data)
    
    cities=df['city'].unique()
    graphs=[]
    for city in cities:
        cityData=df.loc[df['city']==city]
        graph = [
            go.Line(
                x=cityData['date'], # assign x as the dataframe column 'x'
                y=cityData['num_trips']
                )]

        data=go.Data(graph)
        layout=go.Layout(title=city, xaxis={'title':'Date of booking'}, yaxis={'title':'Number of bookings'})
        figure=go.Figure(data=data,layout=layout)
       

        graphs.append(figure)
    ids = [cities[i] for i, _ in enumerate(graphs)]
    graphJSON = json.dumps(graphs, cls=plotly.utils.PlotlyJSONEncoder)
    return [graphJSON,ids]


# add invoice on successul booking 
def createInvoice(id, total):
    cur=mysql.connection.cursor()
    date=pd.to_datetime('today').strftime('%Y-%m-%d')
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
		#result = [dict(zip([key[0] for key in cur.description], row)) for row in rows]
		return jsonify(rows)
	else:
		return jsonify([0])
		
# card details validation
def validateCard(cardNumber,cardDate,cardCVV):
    return (cardNumber=="1111111111111111" and cardDate=="00/00" and cardCVV=="999")
         

if __name__ == '__main__':
    app.run(host='0.0.0.0',debug=True, port=5000)



    # https://dba.stackexchange.com/questions/37014/in-what-data-type-should-i-store-an-email-address-in-database
    # https://medium.com/@PyGuyCharles/python-sql-to-json-and-beyond-3e3a36d32853