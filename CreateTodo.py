import sys
import pymysql
import rds_config
import logging
import json

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


def lambda_handler(event, context):
    data = json.dumps({
        'id': event['todoId'],
        'title': event['title'],
        'description': event['description'],
        'status': event['status'],
        'createdAt': event['createdAt'],
        })
    data_resp = json.loads(data)
    cur = conn.cursor()
    sql = "INSERT INTO `Todo` (`todoId`, `title`, `description`, `status`, `createdAt`) VALUES (%s, %s, %s, %s, %s)"
    cur.execute(sql, (data_resp['id'],data_resp['title'], data_resp['description'], data_resp['status'], data_resp['createdAt']))
    conn.commit()    
    return {
        'statusCode': 200,
        'message':'Todo created successfully!'
        }

