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
    todoId = event['todoId']
    cur = conn.cursor()
    sql = "DELETE FROM Todo WHERE todoId = %s"
    id = (todoId)
    cur.execute(sql, id)
    conn.commit()  
    return {
        'statusCode':200,
        'message':'Successfully deleted'
    }

