from flask import Flask,  request, jsonify,redirect,url_for
import requests
from flask_cors import CORS
import json


app = Flask(__name__)
#  allow cross origin
CORS(app)


@app.route('/search/<loc>', methods=['GET'])
def index(loc):
    return redirect("http://127.0.0.1:5001/search/"+loc, code=302)

# fetch order history of a particular use
@app.route('/orderDetails/<id>', methods=['GET'])
def getOrderDetails(id):
    return redirect("http://127.0.0.1:5002/"+id, code=302)

@app.route('/analytics', methods=['GET'])
def getAnalytics():
    print("hi")
    return redirect("http://127.0.0.1:5003/", code=302)

@app.route('/getBuses/<sourceId>/<destId>',methods=['GET'])
def get_invoice(sourceId,destId):
    return redirect("http://127.0.0.1:5004/getBuses/"+sourceId+"/"+destId, code=302)

# get locations as source
@app.route('/getSources/<destId>', methods=['GET'])
def getSources(destId):
    return redirect("http://127.0.0.1:5004/getSources/"+destId, code=302)

@app.route('/makePayment',methods=['POST'])
def mobile_validate_card():
    param = request.form.to_dict(flat=True)
    return redirect('http://127.0.0.1:5005/makePayment?request={}'.format(param),code=302)
    
@app.route('/registration/', methods=['POST'])
def insertUserDetails():
    param = request.form.to_dict(flat=True)
    return redirect("http://127.0.0.1:5005/registration/?request={}".format(param),code=302)
    
if __name__ == '__main__':
    app.run(host='127.0.0.1',debug=True, port=5000)