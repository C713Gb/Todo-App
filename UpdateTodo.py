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
        'status': event['status']
        })
    data_resp = json.loads(data)
    cur = conn.cursor()
    # sql = "UPDATE Todo SET title=%s, description=%s, status=%s WHERE todoId=%s " % (data_resp['title'], data_resp['description'], data_resp['status'], data_resp['id'])"
    # cur.execute(sql)
    cur.execute("""
       UPDATE Todo
       SET title=%s, description=%s, status=%s
       WHERE todoId=%s
    """, (data_resp['title'], data_resp['description'], data_resp['status'], data_resp['id']))
    conn.commit()    
    return {
        'statusCode': 200,
        'message':'Todo updated successfully!'
        }

