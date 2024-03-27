
from keras.models import load_model
import keras 
from onnx_tf.backend import prepare

def convertTOONNX():
    model = keras.models.load_model('models/snake_model_91.keras')
    onnx_model=prepare(model)
    onnx_model.export('models/snake_model_91..onnx')

    return 

if __name__ == "__main__":
   convertTOONNX()
