package readinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/readinglist")//将所有处理器方法都映射到了“/”这个URL路径上***
public class ReadingListController {
    private ReadingListRepository readingListRepository;

    @Autowired//Autowired 注释，它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作。 通过 @Autowired的使用来消除 set ，get方法。在使用@Autowired之前，我们对一个bean配置起属性时，是这用用的
    public ReadingListController(ReadingListRepository readingListRepository) {//Repository仓库的意思
        this.readingListRepository = readingListRepository;
    }
    @RequestMapping(value = "/{reader}", method = RequestMethod/*请求方法*/.GET)//对于"/{reader}"的请求，通过注解标签下面的方法进行响应
                                    // GET和POST都是从http中获得数据的意思，客户端连接服务器端之后，若想获得web服务器中某个web资源，需遵循一定的通讯格式，HTTP协议就是定义客户端于服务器端进行通讯的格式。
                                     //  method 属性规定如何发送表单数据（表单数据发送到 action 属性所规定的页面）。
                                     //  表单数据可以作为 URL 变量（method="get"）或者 HTTP post （method="post"）的方式来发送。
    public String readersBooks(//方法1，处理/{reader}上的  HTTP GET请求，根据路径里指定的读者。从（通过控制器的构造器注入的）仓库获取Book列表。随后将这个列表塞入模型，用的键是Books，最后返回readingList作为呈现模型的视图逻辑名称
            @PathVariable("reader") String reader, Model model) {
        //                                         模型
        List<Book> readingList =
                readingListRepository.findByReader(reader);
        if (readingList != null) {
            model.addAttribute("Book", readingList);//1.往前台传数据，可以传对象，可以传List，通过el表达式 ${}可以获取到，类似于request.setAttribute("Bool",readingList)效果一样。
        }
        return "readingList";
    }

    @RequestMapping(value = "/{reader}", method = RequestMethod.POST)//get请求有大小限制且不安全
    public String addToReadingList(
            @PathVariable("reader") String reader, Book book) {
        book.setReader(reader);
        readingListRepository.save(book);
        return "redirect:readingList/{reader}";//改变 地址
    }
}


