package com.pingidentity.sync.source.plugin;

import com.unboundid.directory.sdk.sync.api.LDAPSyncSourcePlugin;
import com.unboundid.directory.sdk.sync.types.PostStepResult;
import com.unboundid.directory.sdk.sync.types.SyncOperation;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPInterface;

import java.util.concurrent.atomic.AtomicReference;

public class stashSourceConnection extends LDAPSyncSourcePlugin {
    @Override
    public String getExtensionName() {
        return "stashSourceConnection";
    }

    @Override
    public String[] getExtensionDescription() {
        return new String[]{"Store source connection in the sync operation"};
    }

    @Override
    public PostStepResult postFetch(LDAPInterface sourceConnection, AtomicReference<Entry> fetchedEntryRef, SyncOperation operation) throws LDAPException {
        operation.putAttachment("wkConnectionToSource",sourceConnection);
        return PostStepResult.CONTINUE;
    }

    @Override
    public void toString(StringBuilder stringBuilder) {
    }
}
