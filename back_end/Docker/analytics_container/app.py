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

#  prepare data to analyse bookings for each cities based on timeline
def create_plot(data):

	df = pd.DataFrame(data)
	cities=df['city'].unique()
	graphs=[]
	
	for city in cities:
		cityData=df.loc[df['city']==city]
		fig=go.Figure(go.Line(
				x=cityData['date'], # assign x as the dataframe column 'x'
				y=cityData['num_trips']
				))
		fig.update_layout(
		title=city,
		xaxis_title="Journey Date",
		yaxis_title="No. of Trips",
		font=dict(
			family="Courier New, monospace",
			size=18,
			color="#7f7f7f"
		))
		fig.layout.paper_bgcolor='rgba(0,0,0,0)'
		fig.layout.plot_bgcolor='rgba(0,0,0,0)'
		graphs.append(fig)
	ids = [cities[i] for i, _ in enumerate(graphs)]
	graphJSON = json.dumps(graphs, cls=plotly.utils.PlotlyJSONEncoder)
	return [graphJSON,ids]


# api to analyse the trends in data and create visulizations using plotly library
@app.route('/')
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
    # return "hi"


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True,port=5003)
