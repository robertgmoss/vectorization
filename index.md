---
layout: default
title: Home
---
Welcome! 
========

Vectorization is a database management system for vector spaces.  We provide
a client-server model for you to manage your vectors in your similarity applications.

Installation
------------

###Running from JARs

####Running the Vectorization Server

to run the server:

{% highlight text %}
usage: java -jar vectorization-server-XXX.jar [--help] [-P <arg>] [-V]
    --help         Print this message
 -P,--port <arg>   Port number to use for connection
 -V,--version      Output version information and exit.
{% endhighlight %}

####Running the Vectorization Client

to run the client:

{% highlight text %}
usage: java -jar vectorization-client-XXX.jar [-D <arg>] [-h
       <arg>] [--help] [-P <arg>] [-p <arg>] [-u <arg>] [-V]
 -D,--database <arg>   Database to use.
 -h,--host <arg>       Connect to host.
    --help             Print this message
 -P,--port <arg>       Port number to use for connection
 -p,--password <arg>   Password to use when connecting to server.  If
                       password is not given it's asked from the tty.
 -u,--user <arg>       User for login if not current user.
 -V,--version          Output version information and exit.
{% endhighlight %}

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

###Creating a Database

    use <databasename>

where `<databasename>` is a legal identifier in accordance with the java programming language.

###Creating a Vector Space

     create space <spaceid> with dimensionality <int>

where `<spaceid>` is a legal identifier in accordance with the java programming language, and `<int>`
is an integer that specifies the size of the vectors that can be inserted into this space. For 
example:

    create space myspace with dimensionality 2

###Inserting vectors into a Vector Space

    insert <vectorid> = <vector> into <spaceid>

where `<vectorid>` is a legal identifier in accordance with the java programming language.  `<vector>`
is a comma separated list of real numbers surrounded by square brackets, and `<spaceid>` refers to
the identifier used when a space was created.  For example:

    insert myvector = [0.2, 0.8] into myspace

###Listing Vector Spaces

    list

The list command will display all of the Vector Spaces stored in the current database.

###Showing the contents of a Vector Space

    show <spaceid>[.<vectorid>]

You can view the contents of the entire space by simply passing its identifier to the show command, or
be more specific and show a particular vector by referencing its qualified name.  For example:

    show myspace

or

    show myspace.myvector

###Querying a Vector Space

    find <number> nearest to (<vector>|<spaceid>.<vectorid>) in <spaceid>

We first specify the `<number>` of vectors that we require to be returned, then we can either pass
a `<vector>` or we can reference one with its qualified name.  For example:

    find 10 nearest to [0.5, 0.5] in myspace

or

    find 100 nearest to anotherspace.anothervector in myspace

 
Developing software with the Vectorization Java API
---------------------------------------------------

Add the vectorization-driver to your classpath.

###Establishing a connection
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

{% highlight java %}
String response = StatementBuilders.use("Test").execute(connection);
{% endhighlight %}

{% highlight java %}
String response = StatementBuilders.create("myspace")
                .withDimensionality(2)
                .execute(connection);
{% endhighlight %}

{% highlight java %}
String response = StatementBuilders.insert(Vectors.createVector("myvector", 0.2, 0.8))
                .into("myspace")
                .execute(connection);
{% endhighlight %}

{% highlight java %}
String response = StatementBuilders.list().execute(connection);
{% endhighlight %}

{% highlight java %}
VectorCollection<Vector> result = StatementBuilders.show("myspace")
                .execute(connection);
{% endhighlight %}

{% highlight java %}
VectorCollection<Vector> result = StatementBuilders.show("myspace", "myvector")
                .execute(connection);
{% endhighlight %}

{% highlight java %}
  VectorCollection<Vector> result = StatementBuilders.find(10)
                .nearestTo(Vectors.createVector("query", 0.5, 0.5))
                .in("myspace")
                .execute(connection);
{% endhighlight %}

{% highlight java %}
VectorCollection<Vector> result = StatementBuilders.find(10)
                .nearestTo("anotherspace.anothervector")
                .in("myspace")
                .execute(connection);
{% endhighlight %}
