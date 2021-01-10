package jigsaw;

import javax.xml.stream.XMLStreamException;

public class XMLTabHookDataWriter extends XMLWriter
{
	private TabHookData mData = null;
	
	public XMLTabHookDataWriter(TabHookData data)
	{
		this.mData = data;
		return;
	}
	
	public String write() throws XMLStreamException, Exception
	{
		this.getEventWriter().add(this.getEventFactory().createStartDocument());
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.ROOT));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.NORTHWEST));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.northwest.x)));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.northwest.y)));
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.NORTHWEST));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.NORTHEAST));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.northeast.x)));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.northeast.y)));
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.NORTHEAST));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.SOUTHEAST));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.southeast.x)));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.southeast.y)));
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.SOUTHEAST));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.SOUTHWEST));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.southwest.x)));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.southwest.y)));
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.SOUTHWEST));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.CTRLNORTH));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.ctrlnorth.x)));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.ctrlnorth.y)));
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.CTRLNORTH));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.CTRLEAST));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.ctrleast.x)));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.ctrleast.y)));
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.CTRLEAST));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.CTRLSOUTH));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.ctrlsouth.x)));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.ctrlsouth.y)));
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.CTRLSOUTH));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.CTRLWEST));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.ctrlwest.x)));
		this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.ctrlwest.y)));
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.CTRLWEST));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.NORTH));
		for(int i = 0; i < 9; i++)
		{
			this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.GRIP));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.INDEX, String.valueOf(i)));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.north[i].x)));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.north[i].y)));
			this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.GRIP));
		}
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.NORTH));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.EAST));
		for(int i = 0; i < 9; i++)
		{
			this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.GRIP));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.INDEX, String.valueOf(i)));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.east[i].x)));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.east[i].y)));
			this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.GRIP));
		}
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.EAST));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.SOUTH));
		for(int i = 0; i < 9; i++)
		{
			this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.GRIP));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.INDEX, String.valueOf(i)));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.south[i].x)));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.south[i].y)));
			this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.GRIP));
		}
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.SOUTH));
		
		this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.WEST));
		for(int i = 0; i < 9; i++)
		{
			this.getEventWriter().add(this.getEventFactory().createStartElement("", "", XMLTags.GRIP));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.INDEX, String.valueOf(i)));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.X, String.valueOf(this.mData.west[i].x)));
			this.getEventWriter().add(this.getEventFactory().createAttribute(XMLAttributes.Y, String.valueOf(this.mData.west[i].y)));
			this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.GRIP));
		}
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.WEST));
		
		this.getEventWriter().add(this.getEventFactory().createEndElement("", "", XMLTags.ROOT));
		this.getEventWriter().add(this.getEventFactory().createEndDocument());
		return this.getStringWriter().toString();
	}
}
