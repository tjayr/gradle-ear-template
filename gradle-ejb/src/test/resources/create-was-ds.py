#
# TODO: enter JYTHON code and save
#

import sys,java
from java.util import Properties
from java.io import FileInputStream
from org.python.modules import time
lineSep = java.lang.System.getProperty('line.separator')


def createDataSource():
  uid = "testuser"
  pwd = "test"
  desc = "My Test Alias"
  DataBaseAccessAlias = "MyCellManager01/TestUser"

  secMgrID = AdminConfig.list( 'Security' )

  jaasID = AdminConfig.create( 'JAASAuthData', secMgrID,  \
     [ ['alias', DataBaseAccessAlias],                  \
     ['description', desc],                             \
     ['userId', uid], ['password',pwd]  ] )

  jdbcID = AdminConfig.getid('/ServerCluster:MyCluster/JDBCProvider:MyWasJDBCDriver')
  ds = AdminTask.createDatasource( jdbcID , '[-name testuser -jndiName jdbc/TEST -description "New wsadmin JDBC Datasource" -category "Test Jdbc" -dataStoreHelperClassName com.ibm.websphere.rsadapter.Oracle11gDataStoreHelper -containerManagedPersistence true -componentManagedAuthenticationAlias ' + DataBaseAccessAlias + '  -configureResourceProperties [[URL java.lang.String jdbc:oracle:thin:@IP:1521:dbname]]]')

  AdminConfig.save()

  AdminNodeManagement.syncActiveNodes()

createDataSource()