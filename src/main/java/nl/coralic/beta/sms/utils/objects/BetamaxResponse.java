package nl.coralic.beta.sms.utils.objects;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nl.coralic.beta.sms.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BetamaxResponse extends Response
{

    public BetamaxResponse(boolean responseOke, String response)
    {
	super(responseOke, response);
    }

    public void validateBetamaxResponse()
    {
	try
	{
	    Document doc = getDocument(response.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", ""));
	    Element root = doc.getDocumentElement();
	    root.normalize();
	    if (!root.getElementsByTagName("resultstring").item(0).getFirstChild().getNodeValue().equalsIgnoreCase("success"))
	    {
		responseOke = false;
		String tmpCause = root.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
		if (tmpCause.equalsIgnoreCase(""))
		{
		    errorCode = R.string.ERR_READ_RESP;
		}
		else
		{
		    // For now we use the Betamax errors to show to the user so we need custom error code, all other errors are coming from text.xml
		    errorMessage = tmpCause;
		    errorCode = 9999;
		}
	    }
	}
	catch (Exception e)
	{
	    responseOke = false;
	    errorCode = R.string.ERR_READ_RESP;
	}
    }

    private Document getDocument(String data) throws Exception
    {
	Document doc = null;
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.getBytes());
	doc = builder.parse(byteArrayInputStream);
	return doc;
    }
}