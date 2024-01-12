
## create by rest api

from flask import Flask, request, jsonify
from datetime import datetime
from caculate_results import caculate_Results
from predict import GetDatacenterCharacterestic, predict_datacenter_id
app = Flask(__name__)
@app.route('/process_data', methods=['POST'])
def process_data():
    try:
        client_name = request.json.get('client_name')
        data = request.json.get('data')

        processed_data = process_data_function(data)
        response = {
            'status': 'success',
            'client_name': client_name,
            'processed_data': processed_data
        }
        return jsonify(response)

    except Exception as e:
        # Handle exceptions
        response = {'status': 'error', 'message': str(e)}
        return jsonify(response), 500

def process_data_function(data):
    # Implement your data processing logic here
    # For example, you can print the received data
    nowTime= datetime.now().strftime("%H:%M:%S.%f")[:-3]
    
    task_info = [
        data["TaskID"],
        data["TaskFileSize"],
        data["TaskOutputFileSize"],
        data["TaskFileLength"]
        ]
    task_dict = {
       "TaskID": data["TaskID"],
       "TaskFileSize": data["TaskFileSize"],
       "TaskOutputFileSize": data["TaskOutputFileSize"],
      "TaskFileLength":  data["TaskFileLength"]
    }
    DataCenterId=predict_datacenter_id(task_info)
    DataCenterInfo=GetDatacenterCharacterestic(task_info,DataCenterId)
    results=caculate_Results(task_dict,DataCenterInfo)
    response_data = {
                        "DataCenterId":DataCenterId,
                        "Obtained_Results": results,
                        "ArrivalTime": nowTime,                  
                      }
    return response_data
if __name__ == '__main__':
    app.run(debug=True)
