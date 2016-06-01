package com.forager.meal.utitlity;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class AndroidMultiPartEntity extends MultipartEntity

{


    public AndroidMultiPartEntity(ProgressListener progressListener) {
        super();
    }

    public AndroidMultiPartEntity(final HttpMultipartMode mode) {
        super(mode);
    }

    public AndroidMultiPartEntity(HttpMultipartMode mode,
                                  final String boundary, final Charset charset) {
        super(mode, boundary, charset);
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream));
    }

    public static interface ProgressListener {
        void transferred(long num);
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private long transferred;

        public CountingOutputStream(final OutputStream out) {
            super(out);
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
           /* this.transferred += len;
            this.listener.transferred(this.transferred);*/
        }

        public void write(int b) throws IOException {
            out.write(b);
           /* this.transferred++;
            this.listener.transferred(this.transferred);*/
        }
    }
}