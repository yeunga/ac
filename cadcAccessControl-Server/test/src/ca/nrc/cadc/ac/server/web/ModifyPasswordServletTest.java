/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2015.                            (c) 2015.
 *  Government of Canada                 Gouvernement du Canada
 *  National Research Council            Conseil national de recherches
 *  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 *  All rights reserved                  Tous droits réservés
 *
 *  NRC disclaims any warranties,        Le CNRC dénie toute garantie
 *  expressed, implied, or               énoncée, implicite ou légale,
 *  statutory, of any kind with          de quelque nature que ce
 *  respect to the software,             soit, concernant le logiciel,
 *  including without limitation         y compris sans restriction
 *  any warranty of merchantability      toute garantie de valeur
 *  or fitness for a particular          marchande ou de pertinence
 *  purpose. NRC shall not be            pour un usage particulier.
 *  liable in any event for any          Le CNRC ne pourra en aucun cas
 *  damages, whether direct or           être tenu responsable de tout
 *  indirect, special or general,        dommage, direct ou indirect,
 *  consequential or incidental,         particulier ou général,
 *  arising from the use of the          accessoire ou fortuit, résultant
 *  software.  Neither the name          de l'utilisation du logiciel. Ni
 *  of the National Research             le nom du Conseil National de
 *  Council of Canada nor the            Recherches du Canada ni les noms
 *  names of its contributors may        de ses  participants ne peuvent
 *  be used to endorse or promote        être utilisés pour approuver ou
 *  products derived from this           promouvoir les produits dérivés
 *  software without specific prior      de ce logiciel sans autorisation
 *  written permission.                  préalable et particulière
 *                                       par écrit.
 *
 *  This file is part of the             Ce fichier fait partie du projet
 *  OpenCADC project.                    OpenCADC.
 *
 *  OpenCADC is free software:           OpenCADC est un logiciel libre ;
 *  you can redistribute it and/or       vous pouvez le redistribuer ou le
 *  modify it under the terms of         modifier suivant les termes de
 *  the GNU Affero General Public        la “GNU Affero General Public
 *  License as published by the          License” telle que publiée
 *  Free Software Foundation,            par la Free Software Foundation
 *  either version 3 of the              : soit la version 3 de cette
 *  License, or (at your option)         licence, soit (à votre gré)
 *  any later version.                   toute version ultérieure.
 *
 *  OpenCADC is distributed in the       OpenCADC est distribué
 *  hope that it will be useful,         dans l’espoir qu’il vous
 *  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
 *  without even the implied             GARANTIE : sans même la garantie
 *  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
 *  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
 *  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
 *  General Public License for           Générale Publique GNU Affero
 *  more details.                        pour plus de détails.
 *
 *  You should have received             Vous devriez avoir reçu une
 *  a copy of the GNU Affero             copie de la Licence Générale
 *  General Public License along         Publique GNU Affero avec
 *  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
 *  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
 *                                       <http://www.gnu.org/licenses/>.
 *
 *
 ************************************************************************
 */

package ca.nrc.cadc.ac.server.web;

import ca.nrc.cadc.ac.server.UserPersistence;
import ca.nrc.cadc.auth.HttpPrincipal;

import org.junit.Test;

import javax.security.auth.Subject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.PrivilegedExceptionAction;

import static org.easymock.EasyMock.*;


public class ModifyPasswordServletTest
{
    public void testUnauthorizedSubject(final Subject subject) throws Exception
    {
        @SuppressWarnings("serial")
        final ModifyPasswordServlet testSubject = new ModifyPasswordServlet()
        {
            @Override
            Subject getSubject(final HttpServletRequest request)
            {
                return subject;
            }
        };
        
        final HttpServletRequest mockRequest =
                createMock(HttpServletRequest.class);
        final HttpServletResponse mockResponse =
                createMock(HttpServletResponse.class);
        
        expect(mockRequest.getPathInfo()).andReturn("users/CADCtest").once();
        expect(mockRequest.getMethod()).andReturn("POST").once();
        expect(mockRequest.getRemoteAddr()).andReturn("mysite.com").once();

        mockResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        expectLastCall().once();

        replay(mockRequest, mockResponse);

        Subject.doAs(subject, new PrivilegedExceptionAction<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                testSubject.doPost(mockRequest, mockResponse);
                return null;
            }
        });

        verify(mockRequest, mockResponse);
    }
        
    @Test
    public void testModifyPasswordWithNullSubject() throws Exception
    {
        final Subject subject = null;
        testUnauthorizedSubject(subject);
    }
        
    @Test
    public void testModifyPasswordWithEmptySubject() throws Exception
    {
        final Subject subject = new Subject();;
        testUnauthorizedSubject(subject);
    }
    
    public void testModifyPasswordWithMissingPassword(final String oldPassword, 
            final String newPassword, int responseStatus) throws Exception
    {
        final Subject subject = new Subject();
        subject.getPrincipals().add(new HttpPrincipal("CADCtest"));
    
        @SuppressWarnings("serial")
        final ModifyPasswordServlet testSubject = new ModifyPasswordServlet()
        {
            @Override
            Subject getSubject(final HttpServletRequest request)
            {
                return subject;
            }
        };
        
        final HttpServletRequest mockRequest =
                createMock(HttpServletRequest.class);
        final HttpServletResponse mockResponse =
                createMock(HttpServletResponse.class);
        
        expect(mockRequest.getPathInfo()).andReturn("users/CADCtest").once();
        expect(mockRequest.getMethod()).andReturn("POST").once();
        expect(mockRequest.getRemoteAddr()).andReturn("mysite.com").once();
        expect(mockRequest.getParameter("old_password")).andReturn(oldPassword).once();
        expect(mockRequest.getParameter("new_password")).andReturn(newPassword).once();
    
        mockResponse.setStatus(responseStatus);
        expectLastCall().once();
    
        replay(mockRequest, mockResponse);
    
        Subject.doAs(subject, new PrivilegedExceptionAction<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                testSubject.doPost(mockRequest, mockResponse);
                return null;
            }
        });
    
        verify(mockRequest, mockResponse);
    }
       
    @Test
    public void testModifyPasswordWithMissingOldPassword() throws Exception
    {
        testModifyPasswordWithMissingPassword("", "newPass", HttpServletResponse.SC_BAD_REQUEST);
    }
    
    @Test
    public void testModifyPasswordWithMissingNewPassword() throws Exception
    {
        testModifyPasswordWithMissingPassword("oldPass", "", HttpServletResponse.SC_BAD_REQUEST);
    }
    
    @Test
    public void testModifyPasswordWithInternalServerError() throws Exception
    {
        final String oldPassword = "oldPass";
        final String newPassword = "newPass";
        HttpPrincipal userID = new HttpPrincipal("CADCtest");
        
        final UserPersistence<?> mockUserPersistence =
                createMock(UserPersistence.class);
        mockUserPersistence.setPassword(userID, oldPassword, newPassword);
        expectLastCall().andThrow(new RuntimeException());

        final Subject subject = new Subject();
        subject.getPrincipals().add(userID);
    
        @SuppressWarnings("serial")
        final ModifyPasswordServlet testSubject = new ModifyPasswordServlet()
        {
            @Override
            public void init(final ServletConfig config) throws ServletException
            {
                super.init();

                userPersistence = mockUserPersistence;
            }
           
            @Override
            Subject getSubject(final HttpServletRequest request)
            {
                return subject;
            }
        };
        
        final HttpServletRequest mockRequest =
                createMock(HttpServletRequest.class);
        final HttpServletResponse mockResponse =
                createMock(HttpServletResponse.class);
        
        expect(mockRequest.getPathInfo()).andReturn("users/CADCtest").once();
        expect(mockRequest.getMethod()).andReturn("POST").once();
        expect(mockRequest.getRemoteAddr()).andReturn("mysite.com").once();
        expect(mockRequest.getParameter("old_password")).andReturn(oldPassword).once();
        expect(mockRequest.getParameter("new_password")).andReturn(newPassword).once();
    
        mockResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        expectLastCall().once();
    
        replay(mockRequest, mockResponse, mockUserPersistence);
    
        Subject.doAs(subject, new PrivilegedExceptionAction<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                testSubject.init(null);
                testSubject.doPost(mockRequest, mockResponse);
                return null;
            }
        });
    
        verify(mockRequest, mockResponse, mockUserPersistence);
    }
    
    @Test
    public void testModifyPasswordHappyPath() throws Exception
    {
        final String oldPassword = "oldPass";
        final String newPassword = "newPass";
        HttpPrincipal userID = new HttpPrincipal("CADCtest");
        
        final UserPersistence<?> mockUserPersistence =
                createMock(UserPersistence.class);
        mockUserPersistence.setPassword(userID, oldPassword, newPassword);
        
        final Subject subject = new Subject();
        subject.getPrincipals().add(userID);
    
        @SuppressWarnings("serial")
        final ModifyPasswordServlet testSubject = new ModifyPasswordServlet()
        {
            @Override
            public void init(final ServletConfig config) throws ServletException
            {
                super.init();

                userPersistence = mockUserPersistence;
            }
           
            @Override
            Subject getSubject(final HttpServletRequest request)
            {
                return subject;
            }
        };
        
        final HttpServletRequest mockRequest =
                createMock(HttpServletRequest.class);
        final HttpServletResponse mockResponse =
                createMock(HttpServletResponse.class);
        
        expect(mockRequest.getPathInfo()).andReturn("users/CADCtest").once();
        expect(mockRequest.getMethod()).andReturn("POST").once();
        expect(mockRequest.getRemoteAddr()).andReturn("mysite.com").once();
        expect(mockRequest.getParameter("old_password")).andReturn(oldPassword).once();
        expect(mockRequest.getParameter("new_password")).andReturn(newPassword).once();
    
        replay(mockRequest, mockResponse, mockUserPersistence);
    
        Subject.doAs(subject, new PrivilegedExceptionAction<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                testSubject.init(null);
                testSubject.doPost(mockRequest, mockResponse);
                return null;
            }
        });
    
        verify(mockRequest, mockResponse, mockUserPersistence);
    }
}
