using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.Common;
using System.Linq;
using System.Text;

namespace Winkel
{

    // This class implementes all the CRUD operations. Give all customers, one customer, update customer, delete customer
    class CustomerAdapter
    {
        // Je kan niet afleiden van de klasse DbDataAdapter,
        // dus bewaar je een DbDataAdapter als instantievariabele.

        private DbDataAdapter adapter;
        private DbProviderFactory factory;
        private DbConnection connection;

        public CustomerAdapter(DbProviderFactory factory, DbConnection connection)
        {
            this.factory = factory;
            this.connection = connection;
            adapter = factory.CreateDataAdapter();
            adapter.MissingSchemaAction = MissingSchemaAction.AddWithKey;
            StelSelectCommandIn();
            StelInsertCommandIn();
            StelUpdateCommandIn();
            StelDeleteCommandIn();
            // We willen echt niet alles deleten... 
            // maar van zodra we één rij weghalen, moet het deleteCommand ingesteld zijn!
        }


        private void StelSelectCommandIn()
        {
            adapter.SelectCommand = connection.CreateCommand();
            adapter.SelectCommand.CommandText = ConfigurationManager.AppSettings["SELECT_ALL_CUSTOMERS"];
        }

        private void StelInsertCommandIn()
        {
            adapter.InsertCommand = connection.CreateCommand();
            adapter.InsertCommand.CommandText = ConfigurationManager.AppSettings["INSERT_ONE_CUSTOMER"];
            StelGrosVanParametersInVoorCommand(adapter.InsertCommand);
            adapter.InsertCommand.Parameters.Add(CrParam("@" + DataStorage.CUSTOMERNUMBER, DbType.Int32, DataStorage.CUSTOMERNUMBER, DataRowVersion.Current));
        }

        private void StelDeleteCommandIn()
        {
            adapter.DeleteCommand = connection.CreateCommand();
            adapter.DeleteCommand.CommandText = ConfigurationManager.AppSettings["DELETE_CUSTOMER_WITH_NUMBER"];
            adapter.DeleteCommand.Parameters.Add(CrParam("@" + DataStorage.CUSTOMERNUMBER, DbType.String, DataStorage.CUSTOMERNUMBER));
        }

        // De tekst van updateCommand includeert ALLE kolommen,
        // ook diegene die in dat specifieke geval niet gewijzigd moeten worden.
        // Maar dan cover je tenminste alle soorten aanvragen. 
        // (Zie DataStorageMetDataTable.ZetWijzigingenKlaarVoorCustomer)
        private void StelUpdateCommandIn()
        {
            adapter.UpdateCommand = connection.CreateCommand();
            adapter.UpdateCommand.CommandText = ConfigurationManager.AppSettings["UPDATE_CUSTOMER_WITH_NUMBER"];
            StelGrosVanParametersInVoorCommand(adapter.UpdateCommand);
            adapter.UpdateCommand.Parameters.Add(CrParam("@" + DataStorage.CUSTOMERNUMBER, DbType.String, DataStorage.CUSTOMERNUMBER, DataRowVersion.Original));
        }

        private void StelGrosVanParametersInVoorCommand(DbCommand command)
        {
            command.Parameters.Add(CrParam("@" + DataStorage.ADDRESSLINE1, DbType.String, DataStorage.ADDRESSLINE1, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.ADDRESSLINE2, DbType.String, DataStorage.ADDRESSLINE2, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.CITY, DbType.String, DataStorage.CITY, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.CONTACTFIRSTNAME, DbType.String, DataStorage.CONTACTFIRSTNAME, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.CONTACTLASTNAME, DbType.String, DataStorage.CONTACTLASTNAME, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.COUNTRY, DbType.String, DataStorage.COUNTRY, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.CREDITLIMIT, DbType.Double, DataStorage.CREDITLIMIT, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.CUSTOMERNAME, DbType.String, DataStorage.CUSTOMERNAME, DataRowVersion.Current));
            //command.Parameters.Add(CrParam("@" + DataStorage.CUSTOMERNUMBER, DbType.String, DataStorage.CUSTOMERNUMBER));
            command.Parameters.Add(CrParam("@" + DataStorage.PHONE, DbType.String, DataStorage.PHONE, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.POSTALCODE, DbType.String, DataStorage.POSTALCODE, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.SALESREPEMPLOYEENUMBER, DbType.Int32, DataStorage.SALESREPEMPLOYEENUMBER, DataRowVersion.Current));
            command.Parameters.Add(CrParam("@" + DataStorage.STATE, DbType.String, DataStorage.STATE, DataRowVersion.Current));

        }

        private DbParameter CrParam(String parameterName, DbType type, String column)
        {
            DbParameter parameter = factory.CreateParameter();
            parameter.ParameterName = parameterName;
            parameter.DbType = type;
            parameter.SourceColumn = column;
            return parameter;
        }
        private DbParameter CrParam(String parameterName, DbType type, String column, DataRowVersion version)
        {
            DbParameter parameter = CrParam(parameterName, type, column);
            parameter.SourceVersion = version;
            return parameter;
        }

        // haalt enkel uit de databank, zonder te updaten
        public DataTable GetCustomersWithoutUpdate()
        {
            DataTable table = new DataTable("x");
            adapter.Fill(table);
            return table;
        }

        public void Update(DataTable table)
        {
            adapter.Update(table);
        }

        // Haalt uit databank met update
        public DataTable UpdateAndGetCustomers(DataTable table)
        {
            adapter.Update(table);
            return GetCustomersWithoutUpdate();
        }
    }
}
