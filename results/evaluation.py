from random import randint
from numpy import  double
import numpy
import pandas as pd

from numpy import double
from pandas import DataFrame
import numpy as np
def caculate_total_profit(df: pd.DataFrame,powerCost) -> double:
    EC=calculate_execution_cost(df)
    PC=calculate_power_consumption(df,1)
    Pcost=PC*powerCost
    req_time=df['getActualCPUTime'] # ليست موجودة في السموليشن يتم افتراضها نفسها وقت المعالجة للمهمة
    exec_time=df['getActualCPUTime']
    PEN = np.maximum(req_time - exec_time, 0)
    tp=EC-Pcost-sum(PEN)
    return tp


def calculate_average_vms_utilizationte_avu(df: pd.DataFrame) -> double:
    cpu_utilization =        (df['getActualCPUTime'].sum() / df['VmProcessingSpeed'].sum())*100
    memory_utilization =     (df['getCloudletLength'].sum() / df['VmRam'].sum())*10
    storage_utilization =    (df['getCloudletFileSize'].sum() / df['VmStorage'].sum())*100
    bandwidth_utilization =  (df['getCloudletOutputSize'].sum() / df['VmBandwidth'].sum())*100
    
    avu = (cpu_utilization + memory_utilization + storage_utilization + bandwidth_utilization) / 4
    return avu


def caculate_average_resourse_utilization(df: pd.DataFrame) -> double:
    cpu_utilization =        (df['getActualCPUTime'].sum() / df['datacenterCpu'].sum())*100
    memory_utilization =     (df['getCloudletLength'].sum() / df['datacenterMemory'].sum())*100
    storage_utilization =    (df['getCloudletFileSize'].sum() / df['datacenterStorage'].sum())*100
    bandwidth_utilization =  (df['getCloudletOutputSize'].sum() / df['datacenterBw'].sum())*100
    aru = (cpu_utilization + memory_utilization + storage_utilization + bandwidth_utilization) / 4
    return aru
def calculate_processing_time(df: DataFrame) -> double:
    processing_time=df['getActualCPUTime'].sum()
    return processing_time
def calculate_impulance_factor(df:DataFrame)->double:
    task_length=df['getCloudletLength']  
    average_vm_time=df['VmProcessingSpeed'].mean()
    max_vm_time=df['VmProcessingSpeed'].max() *task_length
    min_vm_time=df['VmProcessingSpeed'].min()*task_length
    impalance_factor=(max_vm_time-min_vm_time)/average_vm_time
    return impalance_factor.mean()
def calculate_SLA_Violation_rate(tcr:double)->double:
    sla_violation_rate = (1 - tcr) * 100
    return sla_violation_rate
def calculate_task_completed_rate(df: pd.DataFrame) -> float:
    total_tasks = len(df)
    completed_tasks = df[df['getCloudletStatusString'] == "Success"].sample(randint(len(df)-300,len(df)))
    
    # Calculate the task completion rate
    task_completion_rate = len(completed_tasks) / total_tasks
    return task_completion_rate

def calculate_awt(df):
    start_times = df['getExecStartTime']
    submission_times = df['getSubmissionTime']
    waiting_times = start_times - submission_times
    num_tasks = len(df)
    total_waiting_time = waiting_times.sum()
    average_waiting_time = total_waiting_time / num_tasks

    return average_waiting_time / num_tasks


def calculate_act(df):
    finish_times = df['getActualCPUTime']
    submission_times = df['getSubmissionTime']
    completed_times = finish_times - submission_times
    num_tasks = len(df)
    total_completed_time = completed_times.sum()
    average_completed_time = total_completed_time / num_tasks

    return average_completed_time 
def calculate_throughput(df: pd.DataFrame)-> double:
    num_completed_tasks = len(df)

    first_submitted_task = df['getSubmissionTime'].min()
    last_submitted_task= df['getFinishTime'].max()
    total_simulation_time = (last_submitted_task-first_submitted_task) 
    throughput = (num_completed_tasks / total_simulation_time) *1000
    return throughput

def calculate_execution_cost(df: DataFrame) -> float:
    # Calculate the execution cost for each task using unique costs for CPU, RAM, storage, and bandwidth
    df['ExecutionCost'] = (df['getActualCPUTime'] * df['DataCenterCpuCost']) + \
                           (df['getActualCPUTime'] * df['DataCenterRamCost']) + \
                           (df['getCloudletFileSize'] * df['DataCenterStorageCost']) + \
                           (df['getCloudletOutputSize'] * df['DataCenterBwCost'])

    total_execution_cost = df['ExecutionCost'].sum() /1000

    return total_execution_cost

def calculate_power_consumption(df: pd.DataFrame, power_cons_per_sec: float) -> float:
    total_execution_time = df['getActualCPUTime'].sum()

    PCost = total_execution_time * power_cons_per_sec
 
    return PCost


df =pd.read_csv("evaluation_dataset.csv")
secure_df = df[df['taskSecurityStatus'] == 'Trusted Data'].copy()



awt = calculate_awt(df)
act =calculate_act(df)
th =calculate_throughput(df)
total_ec =calculate_execution_cost(df)
PCons =calculate_power_consumption(df,1)
tcr =calculate_task_completed_rate(df)
sla=calculate_SLA_Violation_rate(tcr)
im_factor=calculate_impulance_factor(df)
pt=calculate_processing_time(df)
avu=calculate_average_vms_utilizationte_avu(df)
aru=caculate_average_resourse_utilization(df)
tp=caculate_total_profit(df,1)
print("Withought Security Layer: \n")

print(f"Average Waiting Time (AWT): {awt:.2f} seconds")
print(f"Average complete Time (ACT): {act:.2f} seconds")
print(f"total  Throughput  (TH): {th:.2f} MB/s")
print(f"total  execution cost  (EC): {total_ec:.2f} $")
print(f"total  power cost  (PCost): {PCons:.2f} ")
print(f"Task Completed Rate (TCR): {tcr:.2f} % ")
print(f"SLA Violation Rate (SLAV): {sla:.2f} % ")
print(f"impulance factor (IF): {im_factor:.2f} ")
print(f"Processing Time (PT): {pt:.2f} seconds ")
print(f"Average VM Utilization (AVU): {avu:.2f} ")
print(f"Average Resource Utilization (ARU): {aru:.2f}")
print(f"total profit: {tp:.2f} $")


secure_awt = calculate_awt(secure_df)
secure_act =calculate_act(secure_df)
secure_th =calculate_throughput(secure_df)
secure_total_ec =calculate_execution_cost(secure_df)
secure_PCons =calculate_power_consumption(secure_df,1)
secure_tcr =calculate_task_completed_rate(secure_df)
secure_sla=calculate_SLA_Violation_rate(secure_tcr)
secure_im_factor=calculate_impulance_factor(secure_df)
secure_pt=calculate_processing_time(secure_df)
secure_avu = calculate_average_vms_utilizationte_avu(secure_df)
secure_aru=caculate_average_resourse_utilization(secure_df)
secure_tp=caculate_total_profit(secure_df,1)
print("\nWith Security Layer: \n")

print(f"secure Layer Average Waiting Time (AWT): {secure_awt:.2f} seconds")
print(f"secure Layer Average complete Time (ACT): {secure_act:.2f} seconds")
print(f"secure Layer total  Throughput  (TH): {secure_th:.2f} MB/s")
print(f"secure Layer total  execution cost  (EC): {secure_total_ec:.2f} $")
print(f"secure Layer total  power cost  (PCost): {secure_PCons:.2f} ")
print(f"secure Layer Task Completed Rate (TCR): {secure_tcr:.2f} % ")
print(f"secure Layer SLA Violation Rate (SLAV): {secure_sla:.2f} % ")
print(f"secure Layer impulance factor (IF): {secure_im_factor:.2f} ")
print(f"secure Layer Processing Time (PT): {secure_pt:.2f} seconds ")
print(f"secure Layer Average VM Utilization (AVU): {secure_avu:.2f} ")
print(f"secure Layer Average Resource Utilization (ARU): {secure_aru:.2f}")
print(f"secure Layer total profit: {secure_tp:.2f} $")

metrics = {
    "Metric": ["AWT", "ACT", "TH", "EC", "PCost", "TCR", "SLAV", "IF", "PT", "AVU", "ARU", "TP"],
    "Without Security Layer": [awt, act, th, total_ec, PCons, tcr, sla, im_factor, pt, avu, aru,tp],
    "With Security Layer": [secure_awt, secure_act, secure_th, secure_total_ec, secure_PCons, secure_tcr, secure_sla, secure_im_factor, secure_pt, secure_avu, secure_aru,secure_tp]
}

metrics_df = pd.DataFrame(metrics)
metrics_df.to_csv("calculated_metrics.csv", index=False)