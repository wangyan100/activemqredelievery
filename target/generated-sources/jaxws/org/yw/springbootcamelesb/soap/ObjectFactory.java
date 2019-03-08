
package org.yw.springbootcamelesb.soap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.yw.springbootcamelesb.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreateFile_QNAME = new QName("http://soap.springbootcamelesb.yw.org/", "createFile");
    private final static QName _CreateFileResponse_QNAME = new QName("http://soap.springbootcamelesb.yw.org/", "createFileResponse");
    private final static QName _FileCreationStatus_QNAME = new QName("http://soap.springbootcamelesb.yw.org/", "fileCreationStatus");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.yw.springbootcamelesb.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateFile }
     * 
     */
    public CreateFile createCreateFile() {
        return new CreateFile();
    }

    /**
     * Create an instance of {@link CreateFileResponse }
     * 
     */
    public CreateFileResponse createCreateFileResponse() {
        return new CreateFileResponse();
    }

    /**
     * Create an instance of {@link FileCreationStatus }
     * 
     */
    public FileCreationStatus createFileCreationStatus() {
        return new FileCreationStatus();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.springbootcamelesb.yw.org/", name = "createFile")
    public JAXBElement<CreateFile> createCreateFile(CreateFile value) {
        return new JAXBElement<CreateFile>(_CreateFile_QNAME, CreateFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.springbootcamelesb.yw.org/", name = "createFileResponse")
    public JAXBElement<CreateFileResponse> createCreateFileResponse(CreateFileResponse value) {
        return new JAXBElement<CreateFileResponse>(_CreateFileResponse_QNAME, CreateFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileCreationStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.springbootcamelesb.yw.org/", name = "fileCreationStatus")
    public JAXBElement<FileCreationStatus> createFileCreationStatus(FileCreationStatus value) {
        return new JAXBElement<FileCreationStatus>(_FileCreationStatus_QNAME, FileCreationStatus.class, null, value);
    }

}
