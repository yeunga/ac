/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2014.                            (c) 2014.
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
 *  $Revision: 4 $
 *
 ************************************************************************
 */
package ca.nrc.cadc.ac.json;

import ca.nrc.cadc.ac.AC;
import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.ac.GroupProperty;
import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.ac.WriterException;
import ca.nrc.cadc.date.DateUtil;
import ca.nrc.cadc.util.StringBuilderWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.Principal;
import java.text.DateFormat;

public class GroupWriter
{
    /**
     * Write a Group to a StringBuilder.
     * @param group
     * @param builder
     * @throws IOException
     * @throws WriterException
     */
    public static void write(Group group, StringBuilder builder)
        throws IOException, WriterException
    {
        write(group, new StringBuilderWriter(builder));
    }

    /**
     * Write a Group to an OutputStream.
     *
     * @param group Group to write.
     * @param out OutputStream to write to.
     * @throws IOException if the writer fails to write.
     * @throws WriterException
     */
    public static void write(Group group, OutputStream out)
        throws IOException, WriterException
    {
        OutputStreamWriter outWriter;
        try
        {
            outWriter = new OutputStreamWriter(out, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
        write(group, new BufferedWriter(outWriter));
    }

    /**
     * Write a Group to a Writer.
     *
     * @param group Group to write.
     * @param writer  Writer to write to.
     * @throws IOException if the writer fails to write.
     * @throws WriterException
     */
    public static void write(Group group, Writer writer)
        throws IOException, WriterException
    {
        if (group == null)
        {
            throw new WriterException("null group");
        }

        try
        {
            getGroupObject(group).write(writer);
        }
        catch (JSONException e)
        {
            final String error = "Unable to create JSON for Group " +
                                 " because " + e.getMessage();
            throw new WriterException(error, e);
        }
    }

    /**
     *
     * @param group
     * @return
     * @throws WriterException
     */
    public static JSONObject getGroupObject(Group group)
        throws WriterException, JSONException
    {
        return getGroupObject(group, true);
    }

    public static JSONObject getGroupObject(Group group, boolean deepCopy)
        throws WriterException, JSONException
    {
        JSONObject groupObject = new JSONObject();
        groupObject.put("uri", AC.GROUP_URI + group.getID());

        // Group owner
        if (group.getOwner() != null)
        {
            groupObject.put("owner", UserWriter.getUserObject(group.getOwner()));
        }

        if (deepCopy)
        {
            // Group description
            if (group.description != null)
            {
                groupObject.put("description", group.description);
            }

            // lastModified
            if (group.lastModified != null)
            {
                DateFormat df = DateUtil.getDateFormat(DateUtil.IVOA_DATE_FORMAT, DateUtil.UTC);
                groupObject.put("lastModified", df.format(group.lastModified));
            }

            // Group properties
            if (!group.getProperties().isEmpty())
            {
                JSONArray propertiesArray = new JSONArray();
                for (GroupProperty property : group.getProperties())
                {
                    JSONObject propertyObject = new JSONObject();
                    propertyObject.put("property", GroupPropertyWriter.write(property));
                    propertiesArray.put(propertyObject);
                }
                groupObject.put("properties", propertiesArray);
            }

            // Group groupMembers.
            if ((group.getGroupMembers() != null) && (!group.getGroupMembers().isEmpty()))
            {
                JSONArray groupMembersArray = new JSONArray();
                for (Group groupMember : group.getGroupMembers())
                {
                    groupMembersArray.put(getGroupObject(groupMember, false));
                }
                groupObject.put("groupMembers", groupMembersArray);
            }

            // Group userMembers
            if ((group.getUserMembers() != null) && (!group.getUserMembers().isEmpty()))
            {
                JSONArray userMembersArray = new JSONArray();
                for (User<? extends Principal> userMember : group.getUserMembers())
                {
                    userMembersArray.put(UserWriter.getUserObject(userMember));
                }
                groupObject.put("userMembers", userMembersArray);
            }

            // Group groupAdmins.
            if ((group.getGroupAdmins() != null) && (!group.getGroupAdmins().isEmpty()))
            {
                JSONArray groupAdminsArray = new JSONArray();
                for (Group groupAdmin : group.getGroupAdmins())
                {
                    groupAdminsArray.put(getGroupObject(groupAdmin, false));
                }
                groupObject.put("groupAdmins", groupAdminsArray);
            }

            // Group userAdmins
            if ((group.getUserAdmins() != null) && (!group.getUserAdmins().isEmpty()))
            {
                JSONArray userAdminsArray = new JSONArray();
                for (User<? extends Principal> userAdmin : group.getUserAdmins())
                {
                    userAdminsArray.put(UserWriter.getUserObject(userAdmin));
                }
                groupObject.put("userAdmins", userAdminsArray);
            }
        }

        return new JSONObject().put("group", groupObject);
    }

}
