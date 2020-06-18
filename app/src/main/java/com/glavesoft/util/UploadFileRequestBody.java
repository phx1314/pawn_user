package com.glavesoft.util;

import android.app.ProgressDialog;

import com.mdx.framework.service.subscriber.S;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class UploadFileRequestBody extends RequestBody {

    private RequestBody mRequestBody;
    private S s;

    public UploadFileRequestBody(File file, S s) {
        this.mRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        this.s = s;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        //写入
        mRequestBody.writeTo(bufferedSink);

        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            bytesWritten += byteCount;
            ((ProgressDialog) s.getMProgressDialog()).setProgress((int) (bytesWritten * 100 / contentLength()));
            s.getL().onProgress((int) (bytesWritten * 100 / contentLength()));
        }
    }
}