using System;
using System.Collections.Generic;

namespace Winkel
{
    class Program
    {

        private static Random random = new Random();
        static void Main(string[] args)
        {

            DataStorageMetReader storage = new DataStorageMetReader();
            Deel1(storage);


            //Customer toevoegen
            Deel2(storage);

            Deel1(storage);

            //Order toevoegen;
            Deel3(storage);

            Console.ReadKey();
        }



        public static void Deel1(DataStorageMetReader storage)
        {
            List<Customer> customers = storage.GetCustomers();

            Console.WriteLine("lijst heeft lengte " + customers.Count);

            foreach (Customer cust in customers)
            {
                Console.WriteLine(cust.ToString());
            }
            Console.WriteLine("EINDE DATABASELIJST");

        }

        public static void Deel2(DataStorageMetReader storage)
        {

            Customer c = new Customer();
            c.AddressLine1 = "EEN !";
            c.AddressLine2 = "TWEE !";
            c.City = "city";
            c.ContactFirstName = "voornaampje";
            c.ContactLastName = "achternaam contactpersoon";
            c.Country = "BE";
            c.CreditLimit = 50.00;
            c.CustomerName = "naam van klant";
            c.CustomerNumber = 789789789;
            c.Phone = "003212456";
            c.PostalCode = "XM4545";
            c.SalesRepEmployeeNumber = 10101010;
            c.State = "West-Vlaanderen";


            storage.AddCustomer(c);

        }

        public static void Deel3(DataStorageMetReader storage)
        {
            Order order = new Order();
            order.Comments = "dit order heeft geen comments";
            order.CustomerNumber = 99999;
            order.Number = 110000 + random.Next(1, 10000);
            order.Ordered = DateTime.ParseExact("01/12/2018", "dd/MM/yyyy", System.Globalization.CultureInfo.InvariantCulture);
            order.Required = DateTime.ParseExact("25/12/2018", "dd/MM/yyyy", System.Globalization.CultureInfo.InvariantCulture);
            order.Shipped = DateTime.ParseExact("13/12/2018", "dd/MM/yyyy", System.Globalization.CultureInfo.InvariantCulture);
            order.Status = "ok";

            for (int i = 0; i < 5; i++)
            {
                OrderDetail detail = new OrderDetail();
                detail.OrderNumber = order.Number;
                detail.OrderLineNumber = 1 + i;
                detail.Price = 10.0 * (1 + i);
                detail.ProductCode = "" + ((1 + i) * 111);
                detail.Quantity = 100 * (1 + i);
                order.Details.Add(detail);
            }

            storage.AddOrder(order);
        }
    }
}
