/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.vfs.impl.file.event.detectors;

import org.eclipse.che.api.core.jsonrpc.RequestHandlerConfigurator;
import org.eclipse.che.api.core.notification.EventService;
import org.eclipse.che.api.project.shared.dto.event.FileTrackingOperationDto;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.BiConsumer;

/**
 * @author Roman Nikitenko
 */
@Singleton
public class EditorFileOperationHandler {
    private static final String INCOMING_METHOD = "track:editor-file";

    private final EventService eventService;

    @Inject
    public EditorFileOperationHandler(EventService eventService) {
        this.eventService = eventService;
    }

    @Inject
    public void configureHandler(RequestHandlerConfigurator configurator) {
        configurator.newConfiguration()
                    .methodName(INCOMING_METHOD)
                    .paramsAsDto(FileTrackingOperationDto.class)
                    .noResult()
                    .withConsumer(getFileTrackingOperationConsumer());
    }

    private BiConsumer<String, FileTrackingOperationDto> getFileTrackingOperationConsumer() {
        return (String endpointId, FileTrackingOperationDto operation) ->
                eventService.publish(new FileTrackingOperationEvent(endpointId, operation));
    }
}
