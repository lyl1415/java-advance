package geektime.weekone;

import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class MyClassLoader extends ClassLoader{

    public static void main(String[] args) throws Exception{
        //加载字节码文件并创建实例
        Object obj = new MyClassLoader().findClass("Hello").newInstance();
        //调用hello方法
        obj.getClass().getMethod("hello").invoke(obj);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] srcBytes = null;
        //srcBytes = fileToByteArray("E:\\geekbang\\geektime-spring-family-master\\geektime-spring-family-master\\Chapter 1\\helloworld\\src\\main\\resources\\Hello.class");
        try {
            srcBytes = fileToByteArray("Hello.xlass");//把原文件Hello.xlass转化为字节数组
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] desBytes = new byte[srcBytes.length];
        for(int i=0;i<srcBytes.length;i++){
            desBytes[i] = (byte)(255-srcBytes[i]);//解码源数组并存入新数组
        }
        return defineClass(name,desBytes,0,desBytes.length);
    }

    /**
     * 把文件（位于当前应用的resources目录下）转化为字节数组
     * @param fileName
     * @return
     * @throws IOException
     */
    public static byte[] fileToByteArray(String fileName) throws IOException {
        //创建源与目的地
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        File src = classPathResource.getFile();//获得文件的源头，从哪开始传入(源)
        //File src = new File(classPathResource);//获得文件的源头，从哪开始传入(源)
        byte[] dest = null;//最后在内存中形成的字节数组(目的地)
        //选择流
        InputStream is = null;//此流是文件到程序的输入流
        ByteArrayOutputStream baos= null;//此流是程序到新文件的输出流
        //操作(输入操作)
        try {
            is = new FileInputStream(src);//文件输入流
            baos = new ByteArrayOutputStream();//字节输出流，不需要指定文件，内存中存在
            byte[] flush = new byte[1024*10];//设置缓冲，这样便于传输，大大提高传输效率
            int len = -1;//设置每次传输的个数,若没有缓冲的数据大，则返回剩下的数据，没有数据返回-1
            while((len = is.read(flush)) != -1) {
                baos.write(flush,0,len);//每次读取len长度数据后，将其写出
            }
            baos.flush();//刷新管道数据
            dest = baos.toByteArray();//最终获得的字节数组
            return dest;//返回baos在内存中所形成的字节数组
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //释放资源,文件需要关闭,字节数组流无需关闭
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

}
