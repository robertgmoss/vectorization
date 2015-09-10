---
layout: default
title: Home
---
Welcome! 
========

Vectorization is a database management system for vector spaces.  We provide
a client-server model for you to manage your vectors in your similarity applications.

Getting Started
---------------

If it is not yet running, be sure to start the vectorization server. 

###Logging in

To log in with the vectorization client, you can set the username and 
password as a command line option.  Alternatively, you can authenticate
once a connection has been made to the server using the following command:
  
(A default user is created when the server is first run with username 
  `<admin>` with password `<admin>`.)

    login <username> with <password>

You can also use this command to switch users once logged in.

###Using the Java API

####Establishing a connection
Applications start with an instance of Vectorization, which contains a factory method
for establishing a connection.  This default implementation provides a connection
to `<localhost>` on port `<4567>`; to change this it is best to subclass Vectorization
and override the factory method, eg:

{% highlight java %}
public class App extends Vectorization{

    @Override
    public Connection getConnection(){
        return new Connection(<address>, <port>);
    }
}
{% endhighlight %}

This allows us to write applications in the following way:

{% highlight java %}
App application = new App();
Connection connection = application.getConnection();
connection.connect();
{% endhighlight %}

####Writing statements

Once a connection has been established, you can write statements to control
or query the Vectorization database.  This can either be entered as a String:  
    
{% highlight java %}
Statement statement = connection.createStatement();
String result = statement.execute("login admin with admin");
{% endhighlight %}

or you can use the StatementBuilders as described in the CRUD Tutorials

