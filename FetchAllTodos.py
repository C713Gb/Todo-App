import sys
import pymysql
import rds_config
import logging

#rds settings
rds_host = 'database-1.c165gntpkpef.ap-south-1.rds.amazonaws.com'
name = rds_config.db_username
password = rds_config.db_password
db = rds_config.db_name

# logging
logger = logging.getLogger()
logger.setLevel(logging.INFO)

# connect using creds from rds_config.py
try:
    conn = pymysql.connect(host=rds_host, user=name, passwd=password, db=db, connect_timeout=5)
except:
    logger.error("ERROR: Unexpected error: Could not connect to MySql instance.")
    sys.exit()

logger.info("SUCCESS: Connection to RDS mysql instance succeeded")

# array to store values to be returned
records = []

def lambda_handler(event, context):
	cur = conn.cursor()
	cur.execute("SELECT * from Todo")
	conn.commit()
	rows = cur.fetchall()
	records = []
	for row in rows:
		record = {
		'todoId':row[0],
		'title':row[1],
		'description':row[2],
		'status':row[3],
		'createdAt':row[4]
		}
		records.append(record)
	data = {}
	data['data'] = records
	return data

