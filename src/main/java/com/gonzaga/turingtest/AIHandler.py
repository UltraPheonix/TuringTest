import os
import sys
from bardapi import Bard

#Setting up my bardAPI Key
os.environ['_BARD_API_KEY'] = "WQh1k-K0ZS3u6_t_--LNEbyf-cY2DO9MIK0Zn9rblYFl5mG6ptEBUcoN49vkf8KdXgUzhQ."

#My respond method, short and simple
def respond():
	response = Bard().get_answer(sys.argv[1] + "(Extremely short responce plase )")['content']
	print(response)


respond()