from datetime import datetime
import flask
import numpy as np
from keras.models import load_model
from flask import request
from caculate_results import GetDatacenterCharacterestic, caculate_Results
# Load the model efficiently
model = load_model("C:/Users/mohsal/Desktop/app/metalux/cloudsim/ga_lstm/AI_code/models/ga_snake88.keras")

app = flask.Flask(__name__)

@app.route('/get_dataCenterId', methods=['POST'])
def process_data():
   
    client_name = request.json.get('client_name')
    data = request.json.get('data')
    task_info = [
        data["TaskFileSize"],
        data["TaskOutputFileSize"],
        data["TaskFileLength"],
        data["UserLatitude"],
        data["UserLongitude"],
    ]

    # Predict datacenter ID
    data_center_id = predict_datacenter_id(task_info)
    print("DC  #",data_center_id)
   
    # Retrieve DataCenterInfo and calculate results (assuming these functions are defined elsewhere)
    data_center_info = GetDatacenterCharacterestic(data_center_id)
    print("DC #",data_center_id," info: ",data_center_info)
    results = caculate_Results(task_info,data_center_info)
    res={"hi":"by"}
    # Prepare response
    response_data = {
        "DataCenterId": f"{data_center_id}",
        "Obtained_Results": results,
        "ArrivalTime": datetime.now().strftime("%H:%M:%S.%f")[:-3]
    }
    return flask.jsonify(response_data), 200

   
def predict_datacenter_id(task_info):
    features = np.array([[task_info]]).reshape(1, 1, -1)
    return np.argmax(model.predict(features))+3


if __name__ == '__main__':
    
    app.run(debug=False)
