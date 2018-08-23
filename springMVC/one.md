Matrix variables
------------
      RFC 3986 discusses name-value pairs in path segments. 
    In Spring MVC we refer to those as "matrix variables" based on an "old post" by Tim Berners-Lee 
    but they can be also be referred to as URI path parameters.

      Matrix variables can appear in any path segment, each variable separated by semicolon and multiple values separated by 
    comma,e.g. "/cars;color=red,green;year=2012". 
    Multiple values can also be specified through repeated variable names, e.g. "color=red;color=green;color=blue".

      If a URL is expected to contain matrix variables, the request mapping for a controller method must use a URI variable to mask 
    that variable content and ensure the request can be matched successfully independent of matrix variable order and presence. 
    Below is an example:


