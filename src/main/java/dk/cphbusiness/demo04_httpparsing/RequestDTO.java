package dk.cphbusiness.demo04_httpparsing;

import java.util.Map;

public class RequestDTO
{
    
    private String requestLine;
    private Map< String, String > headers;
    private Map< String, String > queryParams;
    private Map< String, String > requestBody;
    
    public RequestDTO()
    {
    }
    
    public RequestDTO( String requestLine, Map< String, String > headers, Map< String, String > queryParams, Map< String, String > requestBody )
    {
        this.requestLine = requestLine;
        this.headers = headers;
        this.queryParams = queryParams;
        this.requestBody = requestBody;
    }
    
    public void setRequestLine( String requestLine )
    {
        this.requestLine = requestLine;
    }
    
    public void setHeaders( Map< String, String > headers )
    {
        this.headers = headers;
    }
    
    public void setQueryParams( Map< String, String > queryParams )
    {
        this.queryParams = queryParams;
    }
    
    public void setRequestBody( Map< String, String > requestBody )
    {
        this.requestBody = requestBody;
    }
    
    public String getRequestLine()
    {
        return this.requestLine;
    }
    
    public Map< String, String > getHeaders()
    {
        return this.headers;
    }
    
    public Map< String, String > getQueryParams()
    {
        return this.queryParams;
    }
    
    public Map< String, String > getRequestBody()
    {
        return this.requestBody;
    }
    
    @Override
    public String toString()
    {
        return
                "requestLine='" + this.requestLine + '\'' +
                        ", headers=" + this.headers +
                        ", queryParams=" + this.queryParams +
                        ", requestBody=" + this.requestBody;
    }
    
}
