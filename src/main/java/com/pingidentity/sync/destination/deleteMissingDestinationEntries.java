package com.pingidentity.sync.destination;

import com.unboundid.directory.sdk.sync.api.JDBCSyncDestination;
import com.unboundid.directory.sdk.sync.types.SyncOperation;
import com.unboundid.directory.sdk.sync.types.TransactionContext;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.Modification;

import java.sql.SQLException;
import java.util.List;

public class deleteMissingDestinationEntries extends JDBCSyncDestination {
    @Override
    public String getExtensionName() {
        return "deleteMissingDestinationEntries";
    }

    @Override
    public String[] getExtensionDescription() {
        return new String[]{"This serves to illustrate how to delete a source entry instead of creating a destination entry"};
    }

    @Override
    public Entry fetchEntry(TransactionContext transactionContext, Entry entry, SyncOperation syncOperation) throws SQLException {
        // this should attempt to retrieve the entry from the destination
        // in real life, you'd want to use a prepared statement
        Entry resultEntry = transactionContext.searchToRawEntry("SELECT * FROM MY_TABLE WHERE ID = '" + entry.getAttributeValue("uid") + "'", "ID");
        return resultEntry;
    }

    @Override
    public void createEntry(TransactionContext transactionContext, Entry entry, SyncOperation syncOperation) throws SQLException {
        // instead of creating the "missing" entry at the destination,
        // retrieve the connection from the source to delete the source entry
        LDAPConnection connection = (LDAPConnection) syncOperation.getAttachment("wkConnectionToSource");
        try {
            connection.delete(entry.getDN());
        } catch (LDAPException e) {
            syncOperation.logError(e.getDiagnosticMessage());
        }
    }

    @Override
    public void modifyEntry(TransactionContext transactionContext, Entry entry, List<Modification> list, SyncOperation syncOperation) throws SQLException {
    }

    @Override
    public void deleteEntry(TransactionContext transactionContext, Entry entry, SyncOperation syncOperation) throws SQLException {
    }
}
