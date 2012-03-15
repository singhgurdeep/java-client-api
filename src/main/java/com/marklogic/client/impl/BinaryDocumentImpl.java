package com.marklogic.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.BinaryDocumentManager;
import com.marklogic.client.DocumentIdentifier;
import com.marklogic.client.Transaction;
import com.marklogic.client.docio.BinaryReadHandle;
import com.marklogic.client.docio.BinaryWriteHandle;
import com.marklogic.client.io.MetadataHandle;

class BinaryDocumentImpl
	extends AbstractDocumentImpl<BinaryReadHandle, BinaryWriteHandle>
	implements BinaryDocumentManager
{
	static final private Logger logger = LoggerFactory.getLogger(BinaryDocumentImpl.class);

	BinaryDocumentImpl(RESTServices services) {
		super(services, "application/x-unknown-content-type");
	}

	public <T extends BinaryReadHandle> T read(DocumentIdentifier docId, T contentHandle, long start, long length) {
		return read(docId, null, contentHandle, start, length, null);
	}
	public <T extends BinaryReadHandle> T read(DocumentIdentifier docId, MetadataHandle metadataHandle, T contentHandle, long start, long length) {
		return read(docId, metadataHandle, contentHandle, start, length, null);
	}
	public <T extends BinaryReadHandle> T read(DocumentIdentifier docId, T contentHandle, long start, long length, Transaction transaction) {
		return read(docId, null, contentHandle, start, length, transaction);
	}
	public <T extends BinaryReadHandle> T read(DocumentIdentifier docId, MetadataHandle metadataHandle, T contentHandle, long start, long length, Transaction transaction) {
		String uri = docId.getUri();
		logger.info("Reading range of binary content for {}",uri);

		// TODO Auto-generated method stub
		return contentHandle;
	}

	private MetadataExtraction metadataExtraction = MetadataExtraction.NONE;
	public MetadataExtraction getMetadataExtraction() {
		return metadataExtraction;
	}

	public void setMetadataExtraction(MetadataExtraction policy) {
		metadataExtraction = policy;	
	}
}