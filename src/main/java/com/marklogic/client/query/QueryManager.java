/*
 * Copyright 2012 MarkLogic Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marklogic.client.query;

import javax.xml.namespace.QName;

import com.marklogic.client.Transaction;
import com.marklogic.client.io.marker.QueryOptionsListReadHandle;
import com.marklogic.client.io.marker.SearchReadHandle;
import com.marklogic.client.io.marker.TuplesReadHandle;
import com.marklogic.client.io.marker.ValuesListReadHandle;
import com.marklogic.client.io.marker.ValuesReadHandle;
import com.marklogic.client.util.RequestLogger;

/**
 * A Query Manager supports searching documents and retrieving values and
 * tuples from lexicons.  To specify the stable part of a query,
 * you write the query options once using QueryOptionsManager
 * before querying using QueryManager.
 */
public interface QueryManager {
    /**
     * The default maximum number of documents in search results.
     */
    static final public long DEFAULT_PAGE_LENGTH = 10;
    /**
     * The default page for search results.
     */
    static final public long START = 1;

    /**
     * The view produced by a query.
     */
    public enum QueryView {
        /**
         * The view specified by the query options.
         */
    	DEFAULT,
    	/**
    	 * A list of result documents with snippets, extracted metadata,
    	 * and so on.
    	 */
    	RESULTS,
    	/**
    	 * Values from different constraints for the matched documents.
    	 */
    	FACETS,
    	/**
    	 * Basic summary information about the matched documents.
    	 */
    	METADATA,
    	/**
    	 * All views that the query can produce.
    	 */
    	ALL;
    }

    /**
     * Returns the maximum number of documents in the result list
     * for queries.
     * @return	the maximum number of results
     */
    public long getPageLength();
    /**
     * Specifies the maximum number of documents in the result list
     * for queries, overriding any maximum specified in the query options.
     * @param length	the maximum number of results
     */
    public void setPageLength(long length);

    /**
     * Returns the type of results produced by queries.
     * @return	the view type for the queries
     */
    public QueryView getView();
    /**
     * Specifies the type of results produced by queries.
     * @param view	the view type for the queries
     */
    public void setView(QueryView view);

    /**
     * Creates a query definition based on a string.  The string
     * has a simple grammar for specifying constraint values
     * for indexes and can be supplied by an end-user in a web form.
     * @return	the string query definition
     */
    public StringQueryDefinition newStringDefinition();
    /**
     * Creates a query definition based on a string and on query options
     * saved previously.
     * @param optionsName	the name of the query options
     * @return	the string query definition
     */
    public StringQueryDefinition newStringDefinition(String optionsName);
    /**
     * Creates a query definition based on a locator such as JSON key,
     * element name, or element and attribute name.
     * @return	the key-value query definition
     */
    public KeyValueQueryDefinition newKeyValueDefinition();
    /**
     * Creates a query definition based on a locator and value and on
     * query options saved previously.
     * @param optionsName	the name of the query options
     * @return	the key-value query definition
     */
    public KeyValueQueryDefinition newKeyValueDefinition(String optionsName);
    /**
     * Creates a query definition based on a structure that identifies
     * clauses and conjunctions.
     * @return	the structured query definition
     */
    public StructuredQueryBuilder newStructuredQueryBuilder();
    /**
     * Creates a query definition based on a structure and on
     * query options saved previously.
     * @param optionsName	the name of the query options
     * @return	the structured query definition
     */
    public StructuredQueryBuilder newStructuredQueryBuilder(String optionsName);

    /**
     * Creates a query definition for deleting documents.
     * @return	the deletion query definition
     */
    public DeleteQueryDefinition newDeleteDefinition();

    /**
     * Creates a query definition for retrieving values based on
     * a named constraint on an index.
     * @param name	the index constraint
     * @return	the values query definition
     */
    public ValuesDefinition newValuesDefinition(String name);
    /**
     * Creates a query definition for retrieving values based on
     * a named constraint and on query options saved previously.
     * @param name	the index constraint
     * @param optionsName	the name of the query options
     * @return	the values query definition
     */
    public ValuesDefinition newValuesDefinition(String name, String optionsName);

    public ValuesListDefinition newValuesListDefinition();
    public ValuesListDefinition newValuesListDefinition(String optionsName);

    /**
     * Creates a locator for a key-value query based on an element name,
     * which may have namespace.
     * @param element	the element name
     * @return	the locator for a key-value query
     */
    public ElementLocator newElementLocator(QName element);
    /**
     * Creates a locator for a key-value query based on an element name
     * and attribute name.
     * @param element	the element name
     * @param attribute	the attribute name
     * @return	the locator for a key-value query
     */
    public ElementLocator newElementLocator(QName element, QName attribute);
    /**
     * Creates a locator for a key-value query based on a JSON key.
     * @param key	the JSON key
     * @return	the locator for a key-value query
     */
    public KeyLocator newKeyLocator(String key);

    /**
     * Searches documents based on query criteria and, potentially, previously
     * saved query options.
     * @param querydef	the definition of query criteria and query options
     * @param searchHandle	a handle for reading the results from the search
     * @return	the handle populated with the results from the search
     */
    public <T extends SearchReadHandle> T search(QueryDefinition querydef, T searchHandle);
    /**
     * Searches documents based on query criteria and, potentially, previously
     * saved query options starting with the specified page listing 
     * document results.
     * @param querydef	the definition of query criteria and query options
     * @param searchHandle	a handle for reading the results from the search
     * @param start	the number of the result page
     * @return	the handle populated with the results from the search
     */
    public <T extends SearchReadHandle> T search(QueryDefinition querydef, T searchHandle, long start);
    /**
     * Searches documents based on query criteria and, potentially, previously
     * saved query options.  The search includes documents modified by the
     * transaction and ignores documents deleted by the transaction.
     * @param querydef	the definition of query criteria and query options
     * @param searchHandle	a handle for reading the results from the search
     * @param transaction	a open transaction for matching documents
     * @return	the handle populated with the results from the search
     */
    public <T extends SearchReadHandle> T search(QueryDefinition querydef, T searchHandle, Transaction transaction);
    /**
     * Searches documents based on query criteria and, potentially, previously
     * saved query options starting with the specified page listing 
     * document results.  The search includes documents modified by the
     * transaction and ignores documents deleted by the transaction.
     * @param querydef	the definition of query criteria and query options
     * @param searchHandle	a handle for reading the results from the search
     * @param start	the number of the result page
     * @param transaction	a open transaction for matching documents
     * @return	the handle populated with the results from the search
     */
    public <T extends SearchReadHandle> T search(QueryDefinition querydef, T searchHandle, long start, Transaction transaction);

    /**
     * Deletes documents based on the query criteria.
     * @param querydef	the definition of query criteria
     */
    public void delete(DeleteQueryDefinition querydef);
    /**
     * Deletes documents based on the query criteria as part
     * of the specified transaction.
     * @param querydef	the definition of query criteria
     * @param transaction	a open transaction for the delete operation
     */
    public void delete(DeleteQueryDefinition querydef, Transaction transaction);

    /**
     * Retrieves values from indexes based on query criteria and, potentially,
     * previously saved query options.
     * @param valdef	the definition of query criteria and query options
     * @param valueHandle	a handle for reading the values for the matched documents
     * @return	the handle populated with the values from the index
     */
    public <T extends ValuesReadHandle> T values(ValuesDefinition valdef, T valueHandle);
    /**
     * Retrieves values from indexes based on query criteria and, potentially,
     * previously saved query options.  The query includes documents modified
     * by the transaction and ignores documents deleted by the transaction.
     * @param valdef	the definition of query criteria and query options
     * @param valueHandle	a handle for reading the values for the matched documents
     * @param transaction	a open transaction for matching documents
     * @return	the handle populated with the values from the index
     */
    public <T extends ValuesReadHandle> T values(ValuesDefinition valdef, T valueHandle, Transaction transaction);

    /**
     * Retrieves combinations of values for the same document from indexes
     * based on query criteria and, potentially, previously saved query options.  
     * @param valdef	the definition of query criteria and query options
     * @param valueHandle	a handle for reading the tuples for the matched documents
     * @return	the handle populated with the tuples from the index
     */
    public <T extends TuplesReadHandle> T tuples(ValuesDefinition valdef, T valueHandle);
    /**
     * Retrieves combinations of values for the same document from indexes
     * based on query criteria and, potentially, previously saved query options.
     * The query includes documents modified by the transaction and ignores
     * documents deleted by the transaction.  
     * @param valdef	the definition of query criteria and query options
     * @param valueHandle	a handle for reading the tuples for the matched documents
     * @param transaction	a open transaction for matching documents
     * @return	the handle populated with the tuples from the index
     */
    public <T extends TuplesReadHandle> T tuples(ValuesDefinition valdef, T valueHandle, Transaction transaction);

    public <T extends ValuesListReadHandle> T valuesList(ValuesListDefinition valdef, T valueHandle);
    public <T extends ValuesListReadHandle> T valuesList(ValuesListDefinition valdef, T valueHandle, Transaction transaction);

    public <T extends QueryOptionsListReadHandle> T optionsList(T valueHandle);
    public <T extends QueryOptionsListReadHandle> T optionsList(T valueHandle, Transaction transaction);

    public MatchDocumentSummary findOne(QueryDefinition querydef);
    public MatchDocumentSummary findOne(QueryDefinition querydef, Transaction transaction);

    /**
     * Starts debugging client requests. You can suspend and resume debugging output
     * using the methods of the logger.
     * 
     * @param logger	the logger that receives debugging output
     */
    public void startLogging(RequestLogger logger);
    /**
     *  Stops debugging client requests.
     */
    public void stopLogging();
}